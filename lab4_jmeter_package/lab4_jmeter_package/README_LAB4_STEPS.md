# Лабораторная работа №4 — JMeter Load & Stress Testing

## 0. Variant

- SSH tunnel: `ssh -N -L 8079:stload.se.ifmo.ru:8080 s374807@se.ifmo.ru -p 2222`
- JMeter host: `localhost`
- JMeter port: `8079`
- token: `496193058`
- user: `-1360753733`
- configs: `1`, `2`, `3`
- prices: `$1400`, `$2100`, `$3500`
- load test: `12 users × 40 req/min/user = 480 req/min`
- response time limit: `670 ms`

---

## 1. Kiểm tra tunnel

Giữ terminal SSH tunnel đang mở, rồi mở terminal khác:

```bash
curl.exe "http://localhost:8079/?token=496193058&user=-1360753733&config=1"
```

Nếu không phải `Connection refused`, tunnel đã đi tới server.

---

## 2. Load testing trong JMeter GUI

Mở file:

```text
jmeter/load-test.jmx
```

Cấu trúc file:

```text
Lab4 Load Testing
├── Config 1 users
├── Config 2 users
├── Config 3 users
├── View Results in Table
├── Aggregate Report
├── Summary Report
└── Graph Results
```

Mỗi Thread Group đã được cấu hình:

```text
Number of Threads = 12
Ramp-up Period = 60 seconds
Duration = 300 seconds
Constant Throughput Timer = 40 req/min/thread
Duration Assertion = 670 ms
Server = localhost
Port = 8079
```

Bấm:

```text
Run → Clear All
Start
```

---

## 3. Load testing bằng command line

Từ thư mục package này:

```bash
jmeter -n -t jmeter/load-test.jmx -l load/result/results.csv -e -o load/report
```

Nếu thư mục `load/report` đã tồn tại, xóa nó trước khi chạy lại.

Phân tích kết quả:

```bash
python scripts/analyze_load_results.py --input load/result/results.csv --out analysis/load
```

Kết quả:

```text
analysis/load/load_summary.csv
analysis/load/load_summary.json
analysis/load/load_response_time_by_config.png
```

Chọn config rẻ nhất có `p95 <= 670 ms`, `error_percent = 0`, không có `HTTP 403/503`.
Nếu không config nào đạt, chọn config có kết quả tốt nhất để tiếp tục stress test và ghi rõ trong отчёт.

---

## 4. Stress testing

Sau khi chọn config, ví dụ chọn `config=2`, sinh các JMX stress test theo từng mức user:

```bash
python scripts/generate_stress_plan.py --config 2 --users 12 --out jmeter/stress_config2_12users.jmx
python scripts/generate_stress_plan.py --config 2 --users 15 --out jmeter/stress_config2_15users.jmx
python scripts/generate_stress_plan.py --config 2 --users 18 --out jmeter/stress_config2_18users.jmx
python scripts/generate_stress_plan.py --config 2 --users 21 --out jmeter/stress_config2_21users.jmx
python scripts/generate_stress_plan.py --config 2 --users 24 --out jmeter/stress_config2_24users.jmx
python scripts/generate_stress_plan.py --config 2 --users 27 --out jmeter/stress_config2_27users.jmx
python scripts/generate_stress_plan.py --config 2 --users 30 --out jmeter/stress_config2_30users.jmx
```

Chạy từng mức:

```bash
jmeter -n -t jmeter/stress_config2_12users.jmx -l stress/result/stress_12.csv
jmeter -n -t jmeter/stress_config2_15users.jmx -l stress/result/stress_15.csv
jmeter -n -t jmeter/stress_config2_18users.jmx -l stress/result/stress_18.csv
jmeter -n -t jmeter/stress_config2_21users.jmx -l stress/result/stress_21.csv
jmeter -n -t jmeter/stress_config2_24users.jmx -l stress/result/stress_24.csv
jmeter -n -t jmeter/stress_config2_27users.jmx -l stress/result/stress_27.csv
jmeter -n -t jmeter/stress_config2_30users.jmx -l stress/result/stress_30.csv
```

Phân tích stress test:

```bash
python scripts/analyze_stress_results.py --inputs stress/result/stress_*.csv --out analysis/stress
```

Kết quả:

```text
analysis/stress/stress_summary.csv
analysis/stress/stress_summary.json
analysis/stress/stress_response_time_vs_load.png
analysis/stress/stress_error_rate_vs_load.png
```

---

## 5. Bảng đưa vào отчёт

### Load testing

| Config | Price | Load, req/min | Average, ms | Median, ms | p90, ms | p95, ms | Error % | Result |
|---|---:|---:|---:|---:|---:|---:|---:|---|

### Stress testing

| Users | Load, req/min | Average, ms | p90, ms | p95, ms | Error % | HTTP 503 | Result |
|---:|---:|---:|---:|---:|---:|---|---|

---

## 6. Ảnh cần chụp cho отчёт

1. SSH tunnel command.
2. JMeter Test Plan tree.
3. HTTP Request của một config.
4. Duration Assertion `670 ms`.
5. Constant Throughput Timer `40 req/min/thread`.
6. Aggregate Report load test.
7. Biểu đồ `load_response_time_by_config.png`.
8. Stress test Thread Group.
9. Biểu đồ `stress_response_time_vs_load.png`.
10. Bảng `stress_summary.csv`.

---

## 7. Cài thư viện Python

```bash
pip install -r requirements.txt
```
