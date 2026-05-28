#!/usr/bin/env python3
"""
Analyze JMeter stress-testing results for Lab 4.

Recommended CSV naming:
  stress/result/stress_12.csv
  stress/result/stress_15.csv
  stress/result/stress_18.csv

Usage:
  python scripts/analyze_stress_results.py --inputs stress/result/stress_*.csv --out analysis/stress
"""
from __future__ import annotations

import argparse
import json
import re
from pathlib import Path

import matplotlib.pyplot as plt
import pandas as pd


def to_bool_success(series: pd.Series) -> pd.Series:
    if series.dtype == bool:
        return series
    return series.astype(str).str.lower().isin(["true", "1", "yes"])


def users_from_filename(path: Path) -> int | None:
    m = re.search(r"stress[_-](\d+)", path.stem, flags=re.IGNORECASE)
    if m:
        return int(m.group(1))
    return None


def summarize_one(path: Path, users: int | None, limit_ms: int, rpm_per_user: int) -> dict:
    df = pd.read_csv(path)
    required = {"elapsed", "success", "responseCode"}
    missing = required - set(df.columns)
    if missing:
        raise ValueError(f"{path} does not contain required columns: {sorted(missing)}")

    df["success_bool"] = to_bool_success(df["success"])

    if users is None:
        users = users_from_filename(path)
    if users is None:
        if "allThreads" in df.columns:
            users = int(df["allThreads"].max())
        else:
            raise ValueError(f"Cannot determine user count for {path}. Rename file as stress_12.csv or pass --users.")

    elapsed = df["elapsed"].astype(float)
    response_codes = df["responseCode"].astype(str)
    error_rate = float((~df["success_bool"]).mean() * 100)
    has_503 = bool((response_codes == "503").any())
    has_403 = bool((response_codes == "403").any())

    row = {
        "file": path.name,
        "users": int(users),
        "load_req_min": int(users * rpm_per_user),
        "samples": int(len(df)),
        "avg_ms": round(float(elapsed.mean()), 2),
        "median_ms": round(float(elapsed.median()), 2),
        "p90_ms": round(float(elapsed.quantile(0.90)), 2),
        "p95_ms": round(float(elapsed.quantile(0.95)), 2),
        "p99_ms": round(float(elapsed.quantile(0.99)), 2),
        "max_ms": round(float(elapsed.max()), 2),
        "error_percent": round(error_rate, 3),
        "has_403": has_403,
        "has_503": has_503,
        "http_codes": response_codes.value_counts().to_dict(),
    }
    row["ok"] = bool(row["p95_ms"] <= limit_ms and row["error_percent"] == 0 and not has_503 and not has_403)
    reasons = []
    if row["p95_ms"] > limit_ms:
        reasons.append(f"p95>{limit_ms}")
    if row["error_percent"] > 0:
        reasons.append("errors>0")
    if has_503:
        reasons.append("HTTP503")
    if has_403:
        reasons.append("HTTP403(parameter error)")
    row["failure_reason"] = "; ".join(reasons)
    return row


def plot_stress(summary: pd.DataFrame, out_dir: Path, limit_ms: int) -> None:
    plt.figure(figsize=(10, 6))
    plt.plot(summary["load_req_min"], summary["avg_ms"], marker="o", label="Average")
    plt.plot(summary["load_req_min"], summary["p95_ms"], marker="o", label="p95")
    plt.axhline(limit_ms, linestyle="--", label=f"Limit {limit_ms} ms")
    plt.xlabel("Load, requests/min")
    plt.ylabel("Response time, ms")
    plt.title("Stress testing: response time vs load")
    plt.grid(alpha=0.3)
    plt.legend()
    plt.tight_layout()
    plt.savefig(out_dir / "stress_response_time_vs_load.png", dpi=160)
    plt.close()

    plt.figure(figsize=(10, 6))
    plt.plot(summary["load_req_min"], summary["error_percent"], marker="o")
    plt.xlabel("Load, requests/min")
    plt.ylabel("Error rate, %")
    plt.title("Stress testing: error rate vs load")
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.savefig(out_dir / "stress_error_rate_vs_load.png", dpi=160)
    plt.close()


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--inputs", nargs="+", required=True, help="Stress CSV files, e.g. stress/result/stress_*.csv")
    parser.add_argument("--users", nargs="*", type=int, help="Optional user counts matching input files")
    parser.add_argument("--out", default="analysis/stress")
    parser.add_argument("--limit-ms", type=int, default=670)
    parser.add_argument("--rpm-per-user", type=int, default=40)
    args = parser.parse_args()

    input_paths = [Path(p) for p in args.inputs]
    user_list = args.users or [None] * len(input_paths)
    if len(user_list) != len(input_paths):
        raise ValueError("--users count must match --inputs count")

    out_dir = Path(args.out)
    out_dir.mkdir(parents=True, exist_ok=True)

    rows = [summarize_one(path, users, args.limit_ms, args.rpm_per_user) for path, users in zip(input_paths, user_list)]
    summary = pd.DataFrame(rows).sort_values("load_req_min")

    fail_rows = summary[summary["ok"] == False]
    first_failure = fail_rows.iloc[0].to_dict() if len(fail_rows) > 0 else None

    summary_for_csv = summary.drop(columns=["http_codes"])
    summary_for_csv.to_csv(out_dir / "stress_summary.csv", index=False)

    with open(out_dir / "stress_summary.json", "w", encoding="utf-8") as f:
        json.dump(
            {
                "limit_ms": args.limit_ms,
                "rpm_per_user": args.rpm_per_user,
                "summary": summary.to_dict(orient="records"),
                "first_failure": first_failure,
            },
            f,
            indent=4,
            ensure_ascii=False,
        )

    plot_stress(summary, out_dir, args.limit_ms)

    print("\nStress testing summary:")
    print(summary_for_csv.to_string(index=False))
    print("\nFirst failure:")
    print(json.dumps(first_failure, indent=4, ensure_ascii=False))
    print(f"\nSaved files to: {out_dir}")


if __name__ == "__main__":
    main()
