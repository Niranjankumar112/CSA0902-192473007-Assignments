# Task 03 – Resource Safety and Debugging

Compile: `javac ManualReport.java TryWithResourcesReport.java`. Run each class separately. Place `store1.txt`, `store2.txt`, and `store3.txt` in the same working directory first; sample input files are included.

The manual version closes each input reader using try-with-resources but manually closes the output writer in `finally`. The second version uses try-with-resources for both readers and writer. This isolates the main writer-cleanup comparison while keeping per-file readers safe in both versions.

**Debugging evidence required by the assignment:** Use your IDE debugger and a deliberately missing/corrupt input file. Record which resources remain open after failure; capture breakpoint screenshots and log the failing file. The code alone does not claim measured file-handle counts.
