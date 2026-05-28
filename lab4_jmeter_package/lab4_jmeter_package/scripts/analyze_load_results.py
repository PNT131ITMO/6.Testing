#!/usr/bin/env python3
"""
Analyze JMeter load-testing results for Lab 4.

Usage:
  python scripts/analyze_load_results.py --input load/result/results.csv --out analysis/load
"""
from __future__ import annotations

import argparse
import json
import re
from pathlib import Path

import matplotlib.pyplot as plt
import pandas as pd


PRICES = {1: 1400, 2: 2100, 3: 3500}


def to_bool_success(series: pd.Series) -> pd.Series:
    if series.dtype == bool:
        return series
    return series.astype(str).str.lower().isin(["true", "1", "yes"])


def read_inputs(paths: list[str]) -> pd.DataFrame:
    frames = []
    for p in paths:
        path = Path(p)
        df = pd.read_csv(path)
        if "source_file" not in df.columns:
            df["source_file"] = path.name
        frames.append(df)
    if not frames:
        raise ValueError("No input CSV files were provided")
    return pd.concat(frames, ignore_index=True)


def detect_config(row: pd.Series) -> int | None:
    text = f"{row.get('label', '')} {row.get('URL', '')} {row.get('source_file', '')}"
    m = re.search(r"(?:config[=_\s-]*|Config\s*)([123])", text, flags=re.IGNORECASE)
    if m:
        return int(m.group(1))
    return None


def summarize_load(df: pd.DataFrame, limit_ms: int) -> pd.DataFrame:
    required = {"elapsed", "success", "responseCode"}
    missing = required - set(df.columns)
    if missing:
        raise ValueError(f"CSV does not contain required columns: {sorted(missing)}")

    df = df.copy()
    df["success_bool"] = to_bool_success(df["success"])
    df["config"] = df.apply(detect_config, axis=1)

    if df["config"].isna().any():
        unknown = df[df["config"].isna()][["label", "source_file"]].head()
        raise ValueError(
            "Cannot detect config number for some rows. "
            f"Check labels or filenames. Examples:\n{unknown}"
        )

    rows = []
    for config, g in df.groupby("config"):
        config = int(config)
        elapsed = g["elapsed"].astype(float)
        response_codes = g["responseCode"].astype(str)
        error_rate = float((~g["success_bool"]).mean() * 100)
        has_503 = bool((response_codes == "503").any())
        has_403 = bool((response_codes == "403").any())

        row = {
            "config": config,
            "price_usd": PRICES.get(config),
            "samples": int(len(g)),
            "load_req_min": 480,
            "avg_ms": round(float(elapsed.mean()), 2),
            "median_ms": round(float(elapsed.median()), 2),
            "p90_ms": round(float(elapsed.quantile(0.90)), 2),
            "p95_ms": round(float(elapsed.quantile(0.95)), 2),
            "max_ms": round(float(elapsed.max()), 2),
            "error_percent": round(error_rate, 3),
            "has_403": has_403,
            "has_503": has_503,
        }

        row["ok_by_p95"] = bool(row["p95_ms"] <= limit_ms and row["error_percent"] == 0 and not has_503 and not has_403)
        row["ok_by_average"] = bool(row["avg_ms"] <= limit_ms and not has_503 and not has_403)
        rows.append(row)

    return pd.DataFrame(rows).sort_values("config")


def choose_config(summary: pd.DataFrame) -> dict:
    ok = summary[summary["ok_by_p95"] == True].sort_values(["price_usd", "p95_ms"])
    if len(ok) > 0:
        selected = ok.iloc[0].to_dict()
        selected["selection_reason"] = "cheapest configuration satisfying p95 <= limit and no errors"
        return selected

    best = summary.sort_values(["has_503", "error_percent", "p95_ms", "price_usd"]).iloc[0].to_dict()
    best["selection_reason"] = "no configuration satisfies strict criterion; selected best observed configuration for stress test"
    return best


def plot_load(summary: pd.DataFrame, out_dir: Path, limit_ms: int) -> None:
    plt.figure(figsize=(10, 6))
    x = summary["config"].astype(str)
    plt.plot(x, summary["avg_ms"], marker="o", label="Average")
    plt.plot(x, summary["p95_ms"], marker="o", label="p95")
    plt.axhline(limit_ms, linestyle="--", label=f"Limit {limit_ms} ms")
    plt.xlabel("Configuration")
    plt.ylabel("Response time, ms")
    plt.title("Load testing: response time by configuration")
    plt.grid(alpha=0.3)
    plt.legend()
    plt.tight_layout()
    plt.savefig(out_dir / "load_response_time_by_config.png", dpi=160)
    plt.close()


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", nargs="+", required=True, help="JMeter CSV file(s)")
    parser.add_argument("--out", default="analysis/load", help="Output directory")
    parser.add_argument("--limit-ms", type=int, default=670)
    args = parser.parse_args()

    out_dir = Path(args.out)
    out_dir.mkdir(parents=True, exist_ok=True)

    df = read_inputs(args.input)
    summary = summarize_load(df, args.limit_ms)
    selected = choose_config(summary)

    summary.to_csv(out_dir / "load_summary.csv", index=False)
    with open(out_dir / "load_summary.json", "w", encoding="utf-8") as f:
        json.dump(
            {
                "limit_ms": args.limit_ms,
                "summary": summary.to_dict(orient="records"),
                "selected_config": selected,
            },
            f,
            indent=4,
            ensure_ascii=False,
        )

    plot_load(summary, out_dir, args.limit_ms)

    print("\nLoad testing summary:")
    print(summary.to_string(index=False))
    print("\nSelected configuration:")
    print(json.dumps(selected, indent=4, ensure_ascii=False))
    print(f"\nSaved files to: {out_dir}")


if __name__ == "__main__":
    main()
