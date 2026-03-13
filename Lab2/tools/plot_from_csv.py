import re
from pathlib import Path

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


_NUM_RE = re.compile(r"^\s*[-+]?(?:\d+\.?\d*|\.\d+)(?:[eE][-+]?\d+)?\s*$")


def read_lab_csv(path: Path, delim: str = ';') -> pd.DataFrame:
    df = pd.read_csv(path, sep=delim, header=None, names=["x", "y"], dtype=str)

    # keep only numeric x rows
    df = df[df["x"].apply(lambda s: bool(_NUM_RE.match(str(s))))].copy()

    df["x"] = df["x"].astype(float)
    df["y"] = pd.to_numeric(df["y"], errors="coerce")  # empty -> NaN
    return df.sort_values("x")


def break_on_jumps(y: np.ndarray, jump_cut: float) -> np.ndarray:
    y2 = y.copy()

    # work only with finite neighbor pairs
    y_f = y2.copy()
    y_f[~np.isfinite(y_f)] = np.nan

    dy = np.abs(np.diff(y_f))
    cut_idx = np.where((dy > jump_cut) & np.isfinite(dy))[0] + 1
    y2[cut_idx] = np.nan
    return y2


def apply_yclip(y: np.ndarray, yclip: float | None) -> np.ndarray:
    if yclip is None:
        return y
    y2 = y.copy()
    m = np.isfinite(y2)
    y2[m] = np.clip(y2[m], -yclip, yclip)
    return y2


def style_axes(yclip: float | None):
    plt.grid(True, alpha=0.3, linewidth=0.6)
    plt.axhline(0, linewidth=0.5, alpha=0.25)
    plt.axvline(0, linewidth=0.5, alpha=0.25)
    if yclip is not None:
        plt.ylim(-yclip, yclip)


def preset_for(name: str) -> dict:
    name = name.lower()
    preset = dict(jump_cut=None, yclip=None)

    if name == "tan":
        preset = dict(jump_cut=300.0, yclip=100.0)
    elif name == "sec":
        preset = dict(jump_cut=300.0, yclip=100.0)
    elif name == "csc":
        preset = dict(jump_cut=300.0, yclip=120.0)
    elif name == "cot":
        preset = dict(jump_cut=300.0, yclip=100.0)
    elif name == "system":
        preset = dict(jump_cut=500.0, yclip=200.0)

    return preset


def plot_csv(path: Path, func_name: str, out_dir: Path, show: bool, delim: str):
    df = read_lab_csv(path, delim=delim)
    x = df["x"].to_numpy(dtype=float)
    y_raw = df["y"].to_numpy(dtype=float)

    p = preset_for(func_name)
    jump_cut = p["jump_cut"]
    yclip = p["yclip"]

    y_plot = y_raw.copy()
    if jump_cut is not None:
        y_plot = break_on_jumps(y_plot, jump_cut)

    y_plot = apply_yclip(y_plot, yclip)

    title = f"{func_name}(x)"

    plt.figure(figsize=(10, 5))
    plt.plot(x, y_plot)
    plt.title(title)
    plt.xlabel("x")
    plt.ylabel("y")
    style_axes(yclip)

    out_dir.mkdir(parents=True, exist_ok=True)
    plt.savefig(out_dir / f"{title}.png", dpi=200, bbox_inches="tight")

    if show:
        plt.show()
    plt.close()


def main():
    output_dir = Path("output")
    plots_dir = Path("plots")
    delim = ";"
    show = False 

    expected = [
        "sin", "cos", "tan", "sec", "csc", "cot",
        "ln", "log2", "log5", "log10",
        "system"
    ]

    if not output_dir.exists():
        raise FileNotFoundError(f"Folder not found: {output_dir.resolve()}")

    found = {p.stem.lower(): p for p in output_dir.glob("*.csv")}

    plotted = []

    for name in expected:
        csv_path = found.get(name)
        if csv_path and csv_path.exists():
            plot_csv(csv_path, func_name=name, out_dir=plots_dir, show=show, delim=delim)
            plotted.append(name)

    for name, csv_path in sorted(found.items()):
        if name not in expected:
            plot_csv(csv_path, func_name=name, out_dir=plots_dir, show=show, delim=delim)
            plotted.append(name)

    print("Plotted:", plotted)
    print("Saved to:", plots_dir.resolve())


if __name__ == "__main__":
    main()