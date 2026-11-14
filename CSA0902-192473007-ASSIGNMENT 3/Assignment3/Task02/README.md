# Task 02 – User-Defined Exceptions and Propagation

Run: `javac StorageDemo.java` then `java StorageDemo`.

The completed TODO wraps `IOException` in `SubmissionStorageException` and preserves the original cause. Expected output:
```
Saved answer01.pdf
Error: Could not store answer02.pdf
Cause: Disk quota exceeded
```

**Analysis to complete:** Add a class diagram and propagation path (storage → service/controller). Explain checked vs unchecked decisions, show `getCause()` and a full chained stack trace. If the cause is not passed to the constructor, `getCause()` is `null`; calling `getMessage()` on that null value would cause `NullPointerException`.
