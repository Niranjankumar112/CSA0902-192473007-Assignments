# Task 01 – Handling Failure in a Bank ATM Simulation

Run: `javac ATMWithdrawal.java` then `java ATMWithdrawal`.

Try at least these paths: valid withdrawal (e.g. account 12345, amount 500), letters instead of account digits, negative account number, negative amount, and amount greater than 5000. The program uses `InputMismatchException`, `IllegalArgumentException`, and `IllegalStateException`; `finally` prints the session-ending message.

**Analysis to complete:** Draw the relevant `Throwable` hierarchy; create the failure-point table; justify catch order; explain why one broad `catch (Exception)` loses useful classification. Note: `Error` conditions such as `OutOfMemoryError` should generally not be treated as routine ATM input failures.
