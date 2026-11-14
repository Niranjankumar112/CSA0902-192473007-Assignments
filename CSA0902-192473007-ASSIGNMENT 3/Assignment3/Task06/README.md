# Task 06 – Deadlock and Inter-Thread Communication

Run `TokenCounter.java` and `DeadlockSafeTransfer.java` separately. TokenCounter completes TODO 1 and TODO 2 using `while`, `wait()` and `notifyAll()` inside synchronized methods. Expected total is 210. The transfer example prevents circular wait by acquiring account locks in ascending ID order.

**Experiments to complete:** Change `while` to `if` in a copy of TokenCounter and explain spurious wakeups/competition; demonstrate a deliberately deadlocking opposite-lock-order version in a controlled run; record counter size over time and explain all four Coffman conditions. Do not leave a deliberately deadlocking process running.
