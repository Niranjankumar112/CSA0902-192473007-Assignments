# Task 04 – Thread Creation and Life Cycle

Run: `javac ScannerStations.java` then `java ScannerStations`.

The runnable-based station threads demonstrate `start()`, `sleep()`, `join()` and `isAlive()`. Output order can vary because the scheduler controls when runnable threads execute. The file also includes a `StationThread extends Thread` implementation for comparison.

**Experiments to record:** Run at least ten times; record finish order. Compare `run()` with `start()`, vary priorities, and test a daemon station. A fixed sleep is only a simple scheduling demonstration, not a performance benchmark.
