from __future__ import annotations

import argparse
import json
import math
import re
from pathlib import Path
from typing import Iterable

import matplotlib.dates as mdates
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd


DEFAULT_LIMIT_MS = 670
DEFAULT_RPM_PER_USER = 40

def expand_inputs(patterns: Iterable[str]) -> list[Path]:
    files: list[Path] = []

    for raw_pattern in patterns:
        raw_pattern = raw_pattern.strip('"').strip("'")
        path = Path(raw_pattern)

        if "*" in raw_pattern or "?" in raw_pattern or "[" in raw_pattern:
            parent = path.parent if str(path.parent) not in ("", ".") else Path(".")
            matches = sorted(parent.glob(path.name))
            files.extend(matches)
        else:
            files.append(path)

    files = [p for p in files if p.exists() and p.is_file()]

    if not files:
        raise FileNotFoundError(
            "No CSV files found. Example:\n"
            'python .\\scripts\\advanced_stress_graphics.py --input ".\\stress\\result\\stress_*.csv"'
        )

    return files


def users_from_filename(path: Path) -> int | None:
    match = re.search(r"(?:stress|users?)[_-]?(\d+)", path.stem, flags=re.IGNORECASE)

    if match:
        return int(match.group(1))

    return None


def success_to_bool(series: pd.Series) -> pd.Series:
    if series.dtype == bool:
        return series

    return series.astype(str).str.lower().isin(["true", "1", "yes", "y"])


def read_jmeter_csvs(files: list[Path], rpm_per_user: int) -> pd.DataFrame:
    frames: list[pd.DataFrame] = []

    for file_index, path in enumerate(files):
        df = pd.read_csv(path)

        if df.empty:
            print(f"Warning: file is empty and will be skipped: {path}")
            continue

        required_columns = {"timeStamp", "elapsed", "success", "responseCode"}
        missing = required_columns - set(df.columns)

        if missing:
            raise ValueError(f"{path} is missing required JMeter columns: {sorted(missing)}")

        df["source_file"] = path.name
        df["file_index"] = file_index

        df["timestamp"] = pd.to_datetime(df["timeStamp"], unit="ms", errors="coerce")
        df["elapsed"] = pd.to_numeric(df["elapsed"], errors="coerce")
        df["success_bool"] = success_to_bool(df["success"])
        df["responseCode"] = df["responseCode"].astype(str)

        users = users_from_filename(path)

        if users is None:
            if "allThreads" in df.columns:
                users = int(pd.to_numeric(df["allThreads"], errors="coerce").max())
            elif "grpThreads" in df.columns:
                users = int(pd.to_numeric(df["grpThreads"], errors="coerce").max())
            else:
                users = 0

        df["target_users"] = users
        df["load_req_min"] = users * rpm_per_user

        if "allThreads" in df.columns:
            df["threads"] = pd.to_numeric(df["allThreads"], errors="coerce").fillna(users)
        elif "grpThreads" in df.columns:
            df["threads"] = pd.to_numeric(df["grpThreads"], errors="coerce").fillna(users)
        else:
            df["threads"] = users

        if "Latency" in df.columns:
            df["Latency"] = pd.to_numeric(df["Latency"], errors="coerce")
        else:
            df["Latency"] = df["elapsed"]

        if "Connect" in df.columns:
            df["Connect"] = pd.to_numeric(df["Connect"], errors="coerce")
        else:
            df["Connect"] = 0

        frames.append(df)

    if not frames:
        raise ValueError("All input files are empty.")

    result = pd.concat(frames, ignore_index=True)
    result = result.dropna(subset=["timestamp", "elapsed"])
    result = result.sort_values("timestamp").reset_index(drop=True)

    result["relative_seconds"] = (
        result["timestamp"] - result["timestamp"].min()
    ).dt.total_seconds()

    return result


def savefig(out_dir: Path, filename: str) -> None:
    out_dir.mkdir(parents=True, exist_ok=True)
    plt.tight_layout()
    plt.savefig(out_dir / filename, dpi=170)
    plt.close()


def empty_plot(out_dir: Path, filename: str, title: str, message: str) -> None:
    plt.figure(figsize=(10, 5))
    plt.title(title)
    plt.text(0.5, 0.5, message, ha="center", va="center", fontsize=12)
    plt.axis("off")
    savefig(out_dir, filename)


def group_by_load(df: pd.DataFrame) -> pd.DataFrame:
    return (
        df.groupby(["target_users", "load_req_min"])
        .agg(
            samples=("elapsed", "count"),
            avg_ms=("elapsed", "mean"),
            median_ms=("elapsed", "median"),
            p90_ms=("elapsed", lambda x: x.quantile(0.90)),
            p95_ms=("elapsed", lambda x: x.quantile(0.95)),
            p99_ms=("elapsed", lambda x: x.quantile(0.99)),
            max_ms=("elapsed", "max"),
            error_percent=("success_bool", lambda x: (~x).mean() * 100),
        )
        .reset_index()
        .sort_values("load_req_min")
    )


def rolling_time_stats(df: pd.DataFrame, freq: str = "30s") -> pd.DataFrame:
    if df.empty:
        return pd.DataFrame()

    indexed = df.set_index("timestamp")

    stats = indexed["elapsed"].resample(freq).agg(["mean", "median", "count"])
    stats["p90"] = indexed["elapsed"].resample(freq).quantile(0.90)
    stats["p95"] = indexed["elapsed"].resample(freq).quantile(0.95)
    stats["p99"] = indexed["elapsed"].resample(freq).quantile(0.99)

    stats["error_percent"] = indexed["success_bool"].resample(freq).apply(
        lambda x: (~x).mean() * 100 if len(x) else np.nan
    )

    stats["threads"] = indexed["threads"].resample(freq).mean()

    return stats.dropna(how="all").reset_index()

def plot_01_latency_and_error_rate(df: pd.DataFrame, out_dir: Path, limit_ms: int) -> None:
    summary = group_by_load(df)

    fig, ax1 = plt.subplots(figsize=(11, 6))

    ax1.plot(
        summary["load_req_min"],
        summary["avg_ms"],
        marker="o",
        label="Среднее время отклика",
    )
    ax1.plot(
        summary["load_req_min"],
        summary["p95_ms"],
        marker="o",
        label="95-й перцентиль",
    )
    ax1.axhline(limit_ms, linestyle="--", label=f"Порог {limit_ms} мс")

    ax1.set_xlabel("Нагрузка, запросов/мин")
    ax1.set_ylabel("Время отклика, мс")
    ax1.grid(alpha=0.3)

    ax2 = ax1.twinx()
    ax2.plot(
        summary["load_req_min"],
        summary["error_percent"],
        marker="s",
        linestyle=":",
        label="Частота ошибок",
    )
    ax2.set_ylabel("Частота ошибок, %")

    lines_1, labels_1 = ax1.get_legend_handles_labels()
    lines_2, labels_2 = ax2.get_legend_handles_labels()
    ax1.legend(lines_1 + lines_2, labels_1 + labels_2, loc="upper left")

    plt.title("Оценка задержки и частоты ошибок")
    savefig(out_dir, "01_latency_and_error_rate.png")

def plot_02_average_latency_by_users(df: pd.DataFrame, out_dir: Path, limit_ms: int) -> None:
    summary = group_by_load(df)

    plt.figure(figsize=(11, 6))

    plt.plot(
        summary["target_users"],
        summary["avg_ms"],
        marker="o",
        label="Среднее время отклика",
    )
    plt.plot(
        summary["target_users"],
        summary["p95_ms"],
        marker="o",
        label="95-й перцентиль",
    )
    plt.axhline(limit_ms, linestyle="--", label=f"Порог {limit_ms} мс")

    plt.xlabel("Количество параллельных пользователей")
    plt.ylabel("Время отклика, мс")
    plt.title("Оценка средней задержки для количества параллельных пользователей")
    plt.grid(alpha=0.3)
    plt.legend()

    savefig(out_dir, "02_average_latency_by_users.png")

def plot_03_response_time_over_time_by_concurrency(
    df: pd.DataFrame,
    out_dir: Path,
    limit_ms: int,
) -> None:
    plt.figure(figsize=(12, 6))

    scatter = plt.scatter(
        df["timestamp"],
        df["elapsed"],
        c=df["threads"],
        s=12,
        alpha=0.65,
    )

    plt.colorbar(scatter, label="Количество потоков / пользователей")
    plt.axhline(limit_ms, linestyle="--", label=f"Порог {limit_ms} мс")

    plt.gca().xaxis.set_major_formatter(mdates.DateFormatter("%H:%M:%S"))
    plt.xlabel("Время")
    plt.ylabel("Время отклика, мс")
    plt.title("Оценка времени отклика во времени от количества параллельных пользователей")
    plt.grid(alpha=0.3)
    plt.legend()

    savefig(out_dir, "03_response_time_over_time_by_concurrency.png")

def plot_04_concurrent_users_distribution(df: pd.DataFrame, out_dir: Path) -> None:
    plt.figure(figsize=(10, 6))

    unique_count = df["threads"].nunique()
    bins = min(30, max(5, unique_count))

    plt.hist(df["threads"], bins=bins, edgecolor="black")

    plt.xlabel("Количество параллельных пользователей / потоков")
    plt.ylabel("Количество запросов")
    plt.title("Распределение параллельных пользователей")
    plt.grid(alpha=0.3)

    savefig(out_dir, "04_concurrent_users_distribution.png")

def plot_05_percentiles_over_time(df: pd.DataFrame, out_dir: Path, limit_ms: int) -> None:
    stats = rolling_time_stats(df, freq="30s")

    if len(stats) < 2:
        empty_plot(
            out_dir,
            "05_response_time_percentiles_over_time.png",
            "Перцентили времени обработки запроса во времени",
            "Недостаточно данных для построения графика.",
        )
        return

    plt.figure(figsize=(12, 6))

    plt.plot(stats["timestamp"], stats["median"], label="p50 / median")
    plt.plot(stats["timestamp"], stats["p90"], label="p90")
    plt.plot(stats["timestamp"], stats["p95"], label="p95")
    plt.plot(stats["timestamp"], stats["p99"], label="p99")
    plt.axhline(limit_ms, linestyle="--", label=f"Порог {limit_ms} мс")

    plt.gca().xaxis.set_major_formatter(mdates.DateFormatter("%H:%M:%S"))
    plt.xlabel("Время")
    plt.ylabel("Время отклика, мс")
    plt.title("Перцентили времени обработки запроса во времени")
    plt.grid(alpha=0.3)
    plt.legend()

    savefig(out_dir, "05_response_time_percentiles_over_time.png")

def plot_06_autocorrelation(df: pd.DataFrame, out_dir: Path) -> None:
    success_df = df[df["success_bool"]].copy()

    if len(success_df) < 20:
        empty_plot(
            out_dir,
            "06_response_time_autocorrelation.png",
            "График автокорреляции для времени отклика",
            "Недостаточно успешных запросов.",
        )
        return

    series = (
        success_df.set_index("timestamp")["elapsed"]
        .resample("1s")
        .median()
        .interpolate(limit_direction="both")
        .dropna()
    )

    max_lag = min(50, max(1, len(series) // 4))

    if len(series) <= max_lag + 2:
        empty_plot(
            out_dir,
            "06_response_time_autocorrelation.png",
            "График автокорреляции для времени отклика",
            "Недостаточно точек после ресэмплинга.",
        )
        return

    autocorr_values = [
        series.autocorr(lag=lag) for lag in range(1, max_lag + 1)
    ]

    confidence = 1.96 / math.sqrt(len(series))

    plt.figure(figsize=(11, 6))

    plt.stem(range(1, max_lag + 1), autocorr_values, basefmt=" ")
    plt.axhline(confidence, linestyle="--", label="95% доверительный интервал")
    plt.axhline(-confidence, linestyle="--")

    plt.xlabel("Лаг, секунд")
    plt.ylabel("Коэффициент автокорреляции")
    plt.title("График автокорреляции для времени отклика")
    plt.grid(alpha=0.3)
    plt.legend()

    savefig(out_dir, "06_response_time_autocorrelation.png")

def plot_07_error_type_distribution(df: pd.DataFrame, out_dir: Path) -> None:
    errors = df[~df["success_bool"]].copy()

    if errors.empty:
        empty_plot(
            out_dir,
            "07_error_type_distribution_over_time.png",
            "Динамика распределения ошибок по типам HTTP-статусов",
            "Ошибок не обнаружено.",
        )
        return

    errors["minute_bin"] = errors["timestamp"].dt.floor("1min")

    table = pd.crosstab(
        errors["responseCode"],
        errors["minute_bin"],
    )

    if table.empty:
        empty_plot(
            out_dir,
            "07_error_type_distribution_over_time.png",
            "Динамика распределения ошибок по типам HTTP-статусов",
            "Недостаточно данных об ошибках.",
        )
        return

    plt.figure(figsize=(12, 5))

    plt.imshow(table.values, aspect="auto")
    plt.colorbar(label="Количество ошибок")

    plt.yticks(range(len(table.index)), table.index)

    xlabels = [ts.strftime("%H:%M") for ts in table.columns]
    step = max(1, len(xlabels) // 10)

    plt.xticks(
        range(0, len(xlabels), step),
        xlabels[::step],
        rotation=45,
    )

    plt.xlabel("Время, минутные интервалы")
    plt.ylabel("HTTP-статус")
    plt.title("Динамика распределения ошибок по типам HTTP-статусов")

    savefig(out_dir, "07_error_type_distribution_over_time.png")

def plot_08_error_clustering(df: pd.DataFrame, out_dir: Path) -> None:
    errors = df[~df["success_bool"]].copy().sort_values("timestamp")

    if len(errors) < 2:
        empty_plot(
            out_dir,
            "08_error_clusters_time.png",
            "Кластеризация ошибок во временном пространстве",
            "Недостаточно ошибок для кластеризации.",
        )
        return

    threshold_seconds = 5
    cluster_ids = []
    current_cluster = 0
    previous_time = None

    for timestamp in errors["timestamp"]:
        if previous_time is None:
            cluster_ids.append(current_cluster)
        else:
            gap = (timestamp - previous_time).total_seconds()

            if gap > threshold_seconds:
                current_cluster += 1

            cluster_ids.append(current_cluster)

        previous_time = timestamp

    errors["cluster"] = cluster_ids

    plt.figure(figsize=(12, 4))

    plt.scatter(
        errors["timestamp"],
        np.ones(len(errors)),
        c=errors["cluster"],
        s=45,
        alpha=0.8,
    )

    plt.gca().xaxis.set_major_formatter(mdates.DateFormatter("%H:%M:%S"))
    plt.yticks([])
    plt.xlabel("Время")
    plt.title("Кластеризация ошибок во временном пространстве")
    plt.grid(alpha=0.3)

    savefig(out_dir, "08_error_clusters_time.png")

def plot_09_response_forecast(df: pd.DataFrame, out_dir: Path, limit_ms: int) -> None:
    success_df = df[df["success_bool"]].copy()
    stats = rolling_time_stats(success_df, freq="30s")
    stats = stats.dropna(subset=["median"])

    if len(stats) < 4:
        empty_plot(
            out_dir,
            "09_response_time_degradation_forecast.png",
            "Прогнозирование деградации времени отклика",
            "Недостаточно данных для прогноза.",
        )
        return

    y = stats["median"].to_numpy(dtype=float)
    x = np.arange(len(y))

    slope, intercept = np.polyfit(x, y, 1)

    future_steps = 6
    future_x = np.arange(len(y), len(y) + future_steps)

    all_x = np.concatenate([x, future_x])
    predicted_y = intercept + slope * all_x

    residuals = y - (intercept + slope * x)
    std = residuals.std() if len(residuals) > 1 else 0

    upper = predicted_y + 1.96 * std
    lower = predicted_y - 1.96 * std

    interval = stats["timestamp"].diff().median()

    if pd.isna(interval):
        interval = pd.Timedelta(seconds=30)

    future_times = [
        stats["timestamp"].iloc[-1] + interval * (i + 1)
        for i in range(future_steps)
    ]

    all_times = list(stats["timestamp"]) + future_times

    plt.figure(figsize=(12, 6))

    plt.plot(
        stats["timestamp"],
        y,
        marker="o",
        label="Фактическая медиана",
    )
    plt.plot(
        all_times,
        predicted_y,
        linestyle="--",
        label="Линейный прогноз",
    )
    plt.fill_between(
        all_times,
        lower,
        upper,
        alpha=0.2,
        label="Интервал неопределенности",
    )
    plt.axhline(limit_ms, linestyle="--", label=f"Порог {limit_ms} мс")

    plt.gca().xaxis.set_major_formatter(mdates.DateFormatter("%H:%M:%S"))
    plt.xlabel("Время")
    plt.ylabel("Время отклика, мс")
    plt.title("Прогнозирование деградации времени отклика")
    plt.grid(alpha=0.3)
    plt.legend()

    savefig(out_dir, "09_response_time_degradation_forecast.png")

def plot_10_thread_error_rate(df: pd.DataFrame, out_dir: Path) -> None:
    if "threadName" not in df.columns:
        empty_plot(
            out_dir,
            "10_error_rate_by_thread.png",
            "Распределение частоты ошибок между потоками",
            "В CSV отсутствует столбец threadName.",
        )
        return

    thread_stats = (
        df.groupby("threadName")
        .agg(
            total_requests=("success_bool", "count"),
            errors=("success_bool", lambda x: (~x).sum()),
        )
        .reset_index()
    )

    thread_stats["error_rate"] = (
        thread_stats["errors"] / thread_stats["total_requests"] * 100
    )

    thread_stats = thread_stats.sort_values("error_rate", ascending=False)

    if len(thread_stats) > 40:
        plot_data = thread_stats.head(40)
    else:
        plot_data = thread_stats

    plt.figure(figsize=(12, 6))

    plt.bar(
        range(len(plot_data)),
        plot_data["error_rate"],
    )

    plt.xticks(
        range(len(plot_data)),
        plot_data["threadName"],
        rotation=90,
        fontsize=7,
    )

    plt.ylabel("Частота ошибок, %")
    plt.xlabel("Поток JMeter")
    plt.title("Распределение частоты ошибок между потоками")
    plt.grid(axis="y", alpha=0.3)

    savefig(out_dir, "10_error_rate_by_thread.png")

def plot_11_performance_anomalies(df: pd.DataFrame, out_dir: Path, limit_ms: int) -> None:
    elapsed = df["elapsed"].astype(float)

    q1 = elapsed.quantile(0.25)
    q3 = elapsed.quantile(0.75)
    iqr = q3 - q1

    anomaly_threshold = q3 + 1.5 * iqr

    if anomaly_threshold <= limit_ms:
        anomaly_threshold = max(anomaly_threshold, elapsed.quantile(0.95))

    plot_df = df.copy()
    plot_df["anomaly"] = plot_df["elapsed"] > anomaly_threshold

    normal = plot_df[~plot_df["anomaly"]]
    anomalies = plot_df[plot_df["anomaly"]]

    plt.figure(figsize=(12, 6))

    plt.scatter(
        normal["timestamp"],
        normal["elapsed"],
        s=10,
        alpha=0.5,
        label="Обычные запросы",
    )

    if not anomalies.empty:
        plt.scatter(
            anomalies["timestamp"],
            anomalies["elapsed"],
            s=18,
            alpha=0.9,
            label="Аномалии",
        )

    plt.axhline(limit_ms, linestyle="--", label=f"Порог {limit_ms} мс")
    plt.axhline(
        anomaly_threshold,
        linestyle=":",
        label=f"Порог аномалии {anomaly_threshold:.1f} мс",
    )

    plt.gca().xaxis.set_major_formatter(mdates.DateFormatter("%H:%M:%S"))
    plt.xlabel("Время")
    plt.ylabel("Время отклика, мс")
    plt.title("Выявление аномалий в производительности")
    plt.grid(alpha=0.3)
    plt.legend()

    savefig(out_dir, "11_performance_anomaly_detection.png")

def plot_12_error_interval_analysis(df: pd.DataFrame, out_dir: Path) -> None:
    errors = df[~df["success_bool"]].copy().sort_values("timestamp")

    if len(errors) < 3:
        empty_plot(
            out_dir,
            "12_error_interval_analysis.png",
            "Анализ временных интервалов между ошибками",
            "Недостаточно ошибок для анализа интервалов.",
        )
        return

    intervals = (
        errors["timestamp"]
        .diff()
        .dt.total_seconds()
        .dropna()
        .reset_index(drop=True)
    )

    window = min(10, max(2, len(intervals) // 3))
    rolling_mean = intervals.rolling(window=window, min_periods=1).mean()

    plt.figure(figsize=(11, 6))

    plt.plot(
        intervals.index + 1,
        intervals,
        marker="o",
        alpha=0.55,
        label="Интервал между ошибками",
    )
    plt.plot(
        rolling_mean.index + 1,
        rolling_mean,
        linewidth=2,
        label=f"Скользящее среднее ({window})",
    )

    plt.xlabel("Номер ошибки")
    plt.ylabel("Интервал между ошибками, секунд")
    plt.title("Анализ временных интервалов между ошибками")
    plt.grid(alpha=0.3)
    plt.legend()

    savefig(out_dir, "12_error_interval_analysis.png")

def save_summary(df: pd.DataFrame, out_dir: Path) -> None:
    summary = group_by_load(df)
    summary.to_csv(out_dir / "advanced_summary_by_load.csv", index=False)

    errors = df[~df["success_bool"]]

    result = {
        "total_samples": int(len(df)),
        "total_errors": int(len(errors)),
        "overall_error_percent": float((len(errors) / len(df)) * 100) if len(df) else 0.0,
        "response_time_ms": {
            "mean": float(df["elapsed"].mean()),
            "median": float(df["elapsed"].median()),
            "p90": float(df["elapsed"].quantile(0.90)),
            "p95": float(df["elapsed"].quantile(0.95)),
            "p99": float(df["elapsed"].quantile(0.99)),
            "max": float(df["elapsed"].max()),
        },
        "error_codes": errors["responseCode"].value_counts().to_dict(),
        "by_load": summary.to_dict(orient="records"),
    }

    with open(out_dir / "advanced_summary.json", "w", encoding="utf-8") as file:
        json.dump(result, file, indent=4, ensure_ascii=False)

def main() -> None:
    parser = argparse.ArgumentParser(
        description="Generate advanced graphics for JMeter stress-test results."
    )

    parser.add_argument(
        "--input",
        nargs="+",
        required=True,
        help='CSV files or wildcard pattern, for example ".\\stress\\result\\stress_*.csv"',
    )
    parser.add_argument(
        "--out",
        default="./analysis/advanced",
        help="Output directory for generated images.",
    )
    parser.add_argument(
        "--limit-ms",
        type=int,
        default=DEFAULT_LIMIT_MS,
        help="Maximum allowed response time in milliseconds.",
    )
    parser.add_argument(
        "--rpm-per-user",
        type=int,
        default=DEFAULT_RPM_PER_USER,
        help="Requests per minute generated by one user.",
    )

    args = parser.parse_args()

    out_dir = Path(args.out)
    out_dir.mkdir(parents=True, exist_ok=True)

    files = expand_inputs(args.input)

    print("Input files:")
    for file in files:
        print(f"  - {file}")

    df = read_jmeter_csvs(files, rpm_per_user=args.rpm_per_user)

    plot_01_latency_and_error_rate(df, out_dir, args.limit_ms)
    plot_02_average_latency_by_users(df, out_dir, args.limit_ms)
    plot_03_response_time_over_time_by_concurrency(df, out_dir, args.limit_ms)
    plot_04_concurrent_users_distribution(df, out_dir)
    plot_05_percentiles_over_time(df, out_dir, args.limit_ms)
    plot_06_autocorrelation(df, out_dir)
    plot_07_error_type_distribution(df, out_dir)
    plot_08_error_clustering(df, out_dir)
    plot_09_response_forecast(df, out_dir, args.limit_ms)
    plot_10_thread_error_rate(df, out_dir)
    plot_11_performance_anomalies(df, out_dir, args.limit_ms)
    plot_12_error_interval_analysis(df, out_dir)

    save_summary(df, out_dir)

    print()
    print("Advanced stress-test graphics were generated successfully.")
    print(f"Output directory: {out_dir.resolve()}")
    print()
    print("Generated PNG files:")

    for png in sorted(out_dir.glob("*.png")):
        print(f"  - {png.name}")


if __name__ == "__main__":
    main()