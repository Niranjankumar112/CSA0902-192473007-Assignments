# Programming in Java – Assignment 2 (CO2)

**Student Name:** T. Niranjan Kumar  
**Register Number:** 192473007  
**Java Version:** JDK 8 or later  
**Required repository name:** `CSA09-192473007-Assignment2`  
**GitHub link:** Add your repository URL after creating it.

## Project contents
- `Assignment_Report.md` – answers, design reasoning, complexity analysis and result tables for Q1–Q7.
- `Assignment_Report.docx` – formatted report for submission.
- `Task01` – String, StringBuilder and StringBuffer benchmark.
- `Task02` – ArrayList, LinkedList and ArrayDeque experiment.
- `Task03` – broken and corrected HashSet equals/hashCode experiments.
- `Task04` – HashMap, TreeMap and LinkedHashMap comparison at 1,000, 10,000 and 100,000 records.
- `Task05` – Generic Leaderboard and Comparator.
- `Task06` – PriorityQueue emergency triage.
- `Task07` – ArrayList-to-ArrayDeque redesign benchmark.
- `raw-data` – CSV headers for recording your own benchmark runs.
- `screenshots` – save your own execution screenshots here.

## Compile and run
Use JDK 8 or later. Open a terminal in the relevant task folder.

```bash
cd Task01
javac StringBuildingBenchmark.java
java StringBuildingBenchmark
```

Repeat the same pattern for each task:
- Task02: `javac ListStructureBenchmark.java` then `java ListStructureBenchmark`
- Task03: `javac Attendance.java` then `java Attendance`; also run `javac BrokenAttendance.java` then `java BrokenAttendance`
- Task04: `javac PharmacyInventory.java` then `java PharmacyInventory`
- Task05: `javac LeaderboardDemo.java` then `java LeaderboardDemo`
- Task06: `javac Triage.java` then `java Triage`
- Task07: `javac PlaylistRedesignBenchmark.java` then `java PlaylistRedesignBenchmark`

## Before submitting
1. Run every program on your own laptop.
2. Replace blank result tables in the report with your own measurements.
3. Repeat benchmarks at least three times; do not rely on one run.
4. Save real output screenshots in each task's `screenshots` folder.
5. Add the GitHub URL to this README and the report.
6. Make at least one commit for each task.
7. Confirm with faculty whether the repository should be public or private with faculty added as collaborator.

**Evidence note:** Benchmark output depends on the computer, JDK, warm-up, garbage collection and background load. The report intentionally leaves timing cells blank rather than inventing results. The programs print measurements that you can record.
