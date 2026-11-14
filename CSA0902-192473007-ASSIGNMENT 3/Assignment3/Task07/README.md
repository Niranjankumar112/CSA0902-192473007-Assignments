# Task 07 – Concurrent, Fault-Tolerant Result Processor

Run:
```bash
javac ResultProcessor.java
java ResultProcessor input 4
```
The `input` folder contains sample valid, invalid, and missing/corrupt cases (missing files are not listed by the directory scan; add an expected-file manifest if you want missing-file counts to be included). Each discovered text file contains one integer score from 0 to 100. A bad file is recorded as a failed result and does not stop other tasks. Results are collected in `ConcurrentLinkedQueue`; work is submitted as `Callable<Result>` and retrieved using `Future<Result>`.

Measure pool sizes 1, 2, 4, and 8 on your laptop. Repeat runs and record actual time in `raw-data/Q7_pool_measurements.csv`. Explain why the best pool size depends on CPU, I/O and workload. This starter processes files present in the input directory; to count absent expected files, compare against a manifest.
