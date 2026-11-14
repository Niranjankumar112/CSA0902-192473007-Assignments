# Programming in Java – Assignment 3 (CO3)

**Student:** T. Niranjan Kumar  
**Register Number:** 192473007  

This report is aligned with the supplied CO3 assignment. See `Assignment_Report.docx` for the full structured report. Run all tasks and fill the blank evidence and measurement fields with actual results. Do not invent timings or screenshots.

## Q1 – Handling Failure in a Bank ATM Simulation

Implementation: `Task01/ATMWithdrawal.java`

- Failure points include non-numeric input, invalid account number, non-positive withdrawal, insufficient balance, and external card-reader/file failures if that component is added.
- Relevant hierarchy: Object → Throwable → Exception → RuntimeException → (InputMismatchException, IllegalArgumentException, IllegalStateException). Errors are separate under Throwable and are not routine user-input failures.
- Catch specific exceptions before broader exceptions. finally runs after normal completion or handled exception, making it suitable for session cleanup/status; it does not guarantee recovery from JVM termination or power loss.
- Joint decision: one catch(Exception) may be acceptable at a top-level boundary for logging, but it hides distinct recovery actions and diagnostic meaning.

## Q2 – User-Defined Exceptions and Propagation

Implementation: `Task02/StorageDemo.java`

- ExamException is a checked base exception. SubmissionStorageException gives storage-layer context while retaining the original IOException as its cause.
- The completed TODO uses: throw new SubmissionStorageException("Could not store " + fileName, e);
- Expected output: Saved answer01.pdf; Error: Could not store answer02.pdf; Cause: Disk quota exceeded.
- If the original cause is not passed, getCause() returns null and the root IOException detail is lost.

## Q3 – Resource Safety, Debugging and Robust Code

Implementation: `Task03/ManualReport.java and TryWithResourcesReport.java`

- Manual close() in finally can be missed if resource creation/assignment is structured poorly; try-with-resources closes initialized resources automatically in reverse order of initialization.
- Suppressed exceptions are close failures retained on a primary exception; inspect them with getSuppressed().
- Both versions process the sample store files; introduce a missing input to test partial failure.

## Q4 – Thread Creation and Life Cycle

Implementation: `Task04/ScannerStations.java`

- Thread subclassing couples the task to Thread; Runnable separates the work from the thread and is generally more flexible.
- Typical life cycle: NEW → RUNNABLE → (BLOCKED/WAITING/TIMED_WAITING as applicable) → TERMINATED. sleep() causes timed waiting; join() waits for another thread to finish.
- Calling run() directly is an ordinary method call on the current thread. start() asks the JVM to schedule a new thread.

## Q5 – Race Conditions in Cinema Seat Booking

Implementation: `Task05/SeatBooking.java`

- The critical section is the check-then-mark-and-increment sequence. Without synchronization, two threads may both observe a seat as free before either marks it booked.
- The completed TODO makes book() synchronized on the SeatBooking instance. Expected output: Seats sold: 10.
- An intrinsic monitor ensures only one thread at a time executes the synchronized instance method for the same object.

## Q6 – Deadlock and Inter-Thread Communication

Implementation: `Task06/TokenCounter.java and DeadlockSafeTransfer.java`

- TokenCounter TODO 1 waits while full, inserts at the circular index, increments count and calls notifyAll(). TODO 2 waits while empty, removes the next token, decrements count, calls notifyAll() and returns it.
- wait() releases the monitor and must be called while owning it. A while loop rechecks the condition after wake-up. sleep() does not release an object's monitor.
- Deadlock prevention uses consistent ascending account-ID lock order, preventing circular wait.

## Q7 – Design a Concurrent, Fault-Tolerant Processor

Implementation: `Task07/ResultProcessor.java`

- The implementation uses ExecutorService, Callable<Result>, Future<Result> and ConcurrentLinkedQueue<Result>. Each file is processed independently; invalid/corrupt content becomes a failure result instead of stopping other file tasks.
- Input text files contain one score from 0 to 100. The sample input includes valid, malformed and out-of-range scores. Missing expected files require an explicit manifest because a directory scan cannot discover files that are absent.
- Pool size is configurable as the second command-line argument. Compare 1, 2, 4 and 8 workers using repeated local measurements.
