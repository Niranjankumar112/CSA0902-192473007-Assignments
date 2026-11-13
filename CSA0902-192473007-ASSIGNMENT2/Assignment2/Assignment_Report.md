# PROGRAMMING IN JAVA – ASSIGNMENT 2 (CO2)
## Strings, Collections, Generics and Design Challenge

**Student Name:** T. Niranjan Kumar  
**Register Number:** 192473007  
**Java Version:** JDK 8 or later  
**Required Repository Name:** `CSA09-192473007-Assignment2`  
**GitHub Link:** https://github.com/Niranjankumar112/CSA0902-192473007-Assignments/edit/main/CSA0902-192473007-ASSIGNMENT2/Assignment2/Assignment_Report.md

> Important: This report provides the design, implementation, expected outputs and measurement procedure. Run every program on your own laptop and replace the measurement placeholders with your actual results. Do not submit machine timings as your own unless you executed them.

---

## Q1 – Building Text Under Load: String, StringBuilder or StringBuffer (6 Marks)

### 1. Prediction
`String` is immutable. Each concatenation with `+` creates a new String result and copies the previous characters plus the new field. Repeating this in a loop can copy the growing prefix many times. `StringBuilder` is mutable and appends into a resizable character buffer, so it normally avoids most intermediate String objects. `StringBuffer` has similar append behaviour but its methods are synchronized, adding synchronization overhead.

For a single ticket with only a few fields, the difference may be too small to matter. For thousands of tickets with many fields, repeated `+` is expected to become slower and create more temporary objects.

### 2. Workloads and design
The benchmark in `Task01/StringBuildingBenchmark.java` tests:
- Workload A: 6 fields × 1,000 tickets – short-ticket baseline.
- Workload B: 50 fields × 10,000 tickets – high-volume workload.
- Workload C: 200 fields × 3,000 tickets – many-field workload.

Each method builds the same fields. A warm-up is performed and the average nanoseconds per ticket are printed. Run the program at least three times and use the median or average of the runs. `System.nanoTime()` measures elapsed time. Memory readings from `Runtime.totalMemory() - Runtime.freeMemory()` are noisy because garbage collection and JVM optimisation affect them; therefore, memory should be treated as an estimate, not an exact object count.

### 3. Implementation
The three implementations are:
- `usingPlus`: repeatedly uses `result = result + field`.
- `usingStringBuilder`: repeatedly calls `append`.
- `usingStringBuffer`: repeatedly calls synchronized `append`.

Compile and run:
```bash
cd Task01
javac StringBuildingBenchmark.java
java StringBuildingBenchmark
```

### 4. Results table – fill with your own laptop measurements

| Workload | Fields | Tickets | String + (ns/ticket) | StringBuilder (ns/ticket) | StringBuffer (ns/ticket) |
|---|---:|---:|---:|---:|---:|
| A | 6 | 1,000 | ______ | ______ | ______ |
| B | 50 | 10,000 | ______ | ______ | ______ |
| C | 200 | 3,000 | ______ | ______ | ______ |

**Intermediate object estimate:** Repeated concatenation creates a new intermediate result for each append operation. For `n` fields, there can be approximately `n` intermediate result strings per ticket, in addition to implementation-dependent temporary objects. Builder and Buffer create a mutable buffer and the final String; internal buffer growth may allocate additional arrays.

### 5. Design decision
Use `StringBuilder` for constructing each ticket locally because the builder is confined to one request/thread and does not need synchronization. Use `StringBuffer` only if a single mutable buffer is genuinely shared between threads and all access must be synchronized. For a shared booking log, a better design is usually a thread-safe logging component or a synchronized/locked append operation around a dedicated log writer, rather than sharing one StringBuffer as an unlimited log.

### 6. Alternative: String.join / StringJoiner / String.format
If the fields are already available as strings, `String.join("|", fields)` or `StringJoiner` can build a delimited ticket clearly. `String.format` is readable for formatted output but may have more formatting overhead and is not the first choice for a high-throughput loop.

For `n` fields and total output length `L`, a builder-based approach is O(L) time and O(L) output/buffer space. Repeated immutable concatenation can copy prefixes repeatedly; with similarly sized fields, copied characters can grow quadratically with the number of fields, approximately O(n²) in this simplified model. Exact cost depends on field lengths and compiler/JVM optimisations.

**Conclusion:** For a tiny ticket, any approach may be acceptable. For many fields and high volume, `StringBuilder` is the recommended per-ticket choice. Use thread-safe logging infrastructure for shared logs.

---

## Q2 – When Is ArrayList an Appropriate Engineering Choice? (4 Marks)

### 1. Prediction
`ArrayList` provides O(1) amortized append and O(1) indexed access. Inserting or removing at the front or middle is O(n), because later elements must shift. `LinkedList` has O(1) insertion/removal once the position is reached, but locating an index is O(n); it also stores node links and has higher per-element memory overhead. `ArrayDeque` is designed for efficient operations at both ends and does not provide indexed access.

### 2. Experiment design
`Task02/ListStructureBenchmark.java` compares sizes 1,000, 5,000 and 10,000. It measures append, front insertion, middle insertion, repeated indexed access and iteration for ArrayList and LinkedList. It also measures a front-operation sequence using ArrayDeque for the changed Up Next condition.

Compile and run:
```bash
cd Task02
javac ListStructureBenchmark.java
java ListStructureBenchmark
```

### 3. Complexity comparison

| Operation | ArrayList | LinkedList | ArrayDeque |
|---|---|---|---|
| Add at end | O(1) amortized | O(1) | O(1) amortized |
| Insert at front | O(n) | O(1) | O(1) |
| Insert in middle by index | O(n) | O(n) to reach position, then O(1) link | Not supported by index |
| Get k-th element | O(1) | O(n) | Not supported by index |
| Iterate all elements | O(n) | O(n) | O(n) |
| Remove from front | O(n) | O(1) | O(1) |

ArrayList stores a backing array of references. LinkedList stores a node per element with previous/next links. ArrayDeque stores elements in a resizable circular array.

### 4. Engineering decision
ArrayList is appropriate when the playlist mostly appends songs, reads songs by index, and iterates often. I would stop recommending it when front/middle insertions and removals dominate. Under the changed Up Next requirement, ArrayDeque is more suitable because adding at either end and removing from the front are efficient. If arbitrary indexed access is also essential, a separate ArrayList view or a different combined design may be needed.

### 5. Alternative: plain array
A fixed plain array can access index k in O(1), but insertion/removal requires manual shifting and is O(n). A dynamically resized array behaves similarly to ArrayList. ArrayList is generally safer and easier because it manages capacity automatically.

### 6. Results – fill after execution

| Size | Structure | Add end (ns) | Front insert (ns) | Middle insert (ns) | 10,000 gets (ns) | Iterate (ns) |
|---:|---|---:|---:|---:|---:|---:|
| 1,000 | ArrayList | ___ | ___ | ___ | ___ | ___ |
| 1,000 | LinkedList | ___ | ___ | ___ | ___ | ___ |
| 5,000 | ArrayList | ___ | ___ | ___ | ___ | ___ |
| 5,000 | LinkedList | ___ | ___ | ___ | ___ | ___ |
| 10,000 | ArrayList | ___ | ___ | ___ | ___ | ___ |
| 10,000 | LinkedList | ___ | ___ | ___ | ___ | ___ |

---

## Q3 – Engineer the hashCode–equals Contract (6 Marks)

### 1. Contract
If two objects are equal according to `equals`, they must return the same `hashCode`. The reverse is not required: two unequal objects may have the same hash code. HashSet first uses the hash code to find a bucket and then uses equals to check objects in that bucket. If equals is overridden but hashCode is not, equal objects can be placed in different buckets and duplicate logical records may remain.

### 2. Tests and predictions

| Test | Input | Predicted size after test | Reason |
|---|---|---:|---|
| 1 | Add the exact same Student object twice | 1 | Same reference and equality |
| 2 | Add two different objects with same regNo | 1 for that registration | equals and hashCode use regNo |
| 3 | Add a new registration number | 2 after adding it to the existing two-record set | Different registration remains unique |

The completed `Student` class uses a final registration number and implements `hashCode()` with the same field used by `equals()`. Keeping identity fields immutable avoids corrupting the hash-based collection after insertion.

### 3. Deliberately broken experiment
`Task03/BrokenAttendance.java` overrides `equals()` using registration number but deliberately does not override `hashCode()`. Run it first to observe the contract problem. On typical JVM runs, the two separate objects with the same registration number are assigned different identity hash codes and may occupy different buckets, so both remain in the set. A hash collision can affect the exact result, so the important point is that the broken class gives no guarantee required by the contract.

Compile and run:
```bash
cd Task03
javac BrokenAttendance.java
java BrokenAttendance
```

### 4. Complete-the-code solution
```java
@Override
public int hashCode() {
    return Objects.hash(regNo);
}
```

Compile and run:
```bash
cd Task03
javac Attendance.java
java Attendance
```

Expected output:
```text
Test 1 - same object twice: 1
Test 2 - duplicate registration removed: 2
Test 3 - three unique registrations: 3
```
The iteration order of HashSet is not guaranteed, so the last three printed student rows may appear in a different order.

### 5. What if a hash field changes?
If a field used by `hashCode()` changes after insertion, the object may remain in the bucket selected by its old hash. A later lookup or removal calculates the new hash and may not find it. The safest design is to use immutable fields for equality and hashing.

### 6. Alternative structures
A `TreeSet<Student>` with a comparator on registration number can de-duplicate in O(log n) per insertion, provided compare returns zero exactly when the registration numbers should be considered equal. A `HashMap<String, Student>` keyed by registration number gives O(1) average lookup/insert and stores one student per key. Both use O(n) space for n unique students.

**Conclusion:** The corrected class follows the equals–hashCode contract and uses an immutable key.

---

## Q4 – Choosing a Map for a Pharmacy Inventory (6 Marks)

### 1. Map comparison

| Map | put/get | Iteration order | Best use here |
|---|---|---|---|
| HashMap | O(1) average | No guaranteed order | Fast stock lookup |
| TreeMap | O(log n) | Sorted by key | Medicine-code report |
| LinkedHashMap | O(1) average | Insertion order | Added-order report |

### 2. Implementation
`Task04/PharmacyInventory.java` inserts five medicine records into all three maps, performs a lookup and measures 100,000 lookups.

Compile and run:
```bash
cd Task04
javac PharmacyInventory.java
java PharmacyInventory
```

The TreeMap prints keys in code order. LinkedHashMap preserves the insertion order. HashMap does not promise either order. If HashMap is selected for a sorted report, the entries must be copied and sorted before printing, adding O(n log n) sorting work.

### 3. Scalability and measurements
The program provides a basic timing comparison for 100,000 lookups. Repeat with larger data sets (for example 1,000, 10,000 and 100,000 medicine codes) before making a deployment decision. For one lookup, a reasonable classroom target can be set as less than 1 ms in the local in-memory test; actual production response time also includes network, database and service overhead.

| Number of medicines | HashMap 100k lookups (ns) | TreeMap 100k lookups (ns) | LinkedHashMap 100k lookups (ns) |
|---:|---:|---:|---:|
| 1,000 | ___ | ___ | ___ |
| 10,000 | ___ | ___ | ___ |
| 100,000 | ___ | ___ | ___ |

### 4. Deployment decision
For a system needing frequent lookups and both reports, I would keep the primary inventory in a HashMap for average constant-time lookup and generate the two reports by sorting a snapshot for code order and using a LinkedHashMap or a separate insertion-order list for insertion order. If reports are frequent and must always be available in order, maintaining separate ordered views may be justified, at the cost of extra memory and update work.

Hashtable is generally not preferred in new code because it is a legacy synchronized collection. Modern code usually uses HashMap with explicit synchronization/concurrent collections when thread safety is required.

### 5. Alternative: sorted array
A sorted array supports O(log n) lookup with binary search and O(n) insertion because elements must shift. Producing the code-order report is O(n) because the array is already sorted. It uses O(n) storage and is useful when data changes rarely but lookups/reports are frequent.

---

## Q5 – Derive a Generic Ranking Component from a Principle (7 Marks)

### 1. Design decision
A separate Comparator is better than hard-coding one natural order in Team because the same Leaderboard component can rank teams, players, colleges or quiz teams using different rules. `Leaderboard<T>` stores generic items and accepts a `Comparator<? super T>`. This allows a comparator for T or one of its supertypes.

### 2. Comparator rule
The ranking order is:
1. Points descending.
2. Goal difference descending.
3. Team name ascending (A–Z).

The implementation uses `Comparator.comparingInt`, `reversed()` and `thenComparing()`. It avoids subtraction such as `b.points - a.points`, which can overflow for extreme integer values.

### 3. Generic class and wildcard
`ranking()` copies the internal list and sorts the copy, so calling ranking does not change the original insertion order. `addAll(List<? extends T>)` accepts a list of T or a subtype of T (producer extends).

### 4. Implementation
See `Task05/LeaderboardDemo.java`.

Compile and run:
```bash
cd Task05
javac LeaderboardDemo.java
java LeaderboardDemo
```

Expected output:
```text
[Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]
With extra tie: [Eagles(9,6), Ants(9,4), Bisons(9,4), Panthers(9,4), Cobras(6,1)]
```

Bisons and Panthers have equal points and goal difference. Their names are compared alphabetically, so Bisons comes before Panthers.

### 5. Alternative concepts and complexity
If Team implements Comparable<Team>, one natural ranking can be used by `Collections.sort`, but a different ranking requires another comparator or changes to the class. The Comparator approach keeps ranking policy outside the data class and is easier to reuse.

Sorting once with a comparator is O(n log n) time. Sorting three times with stable sorts (name, then goal difference, then points) is approximately 3 × O(n log n), still O(n log n) asymptotically but with more passes. Copying the list uses O(n) extra space. The original list remains unchanged.

---

## Q6 – Priority, Order and Fairness in a Queue (8 Marks)

### 1. Design
A plain FIFO Queue treats the earliest arrival as next, regardless of medical severity. It cannot directly satisfy the requirement that severity 1 must be treated before severity 5. A PriorityQueue with a comparator is suitable.

The comparator sorts severity ascending (1 is most severe), then arrival counter ascending. The monotonically increasing counter preserves first-come-first-served order among patients with equal severity.

### 2. Implementation and output
See `Task06/Triage.java`.

Compile and run:
```bash
cd Task06
javac Triage.java
java Triage
```

Expected output:
```text
Meena(S1) Kumar(S1) Lata(S2) Sara(S2) Ravi(S3) John(S3) Vijay(S4) Asha(S5)
```

Printing a PriorityQueue directly does not guarantee sorted treatment order. Repeatedly calling `poll()` returns the next item in priority order.

### 3. Complexity
Adding a patient to PriorityQueue is O(log n); removing the highest-priority patient is O(log n); peeking is O(1). The queue stores O(n) patients.

| Design | Admit | Serve next | Notes |
|---|---|---|---|
| PriorityQueue | O(log n) | O(log n) | Good general priority handling |
| Sorted LinkedList | O(n) | O(1) | Find insertion position by scanning |
| Five FIFO queues | O(1) | O(1) to inspect/remove next non-empty highest-priority queue | Very efficient for fixed five severity levels |

### 4. Starvation and fairness
A severity-5 patient may wait for a very long time if new severity-1 to severity-4 patients keep arriving. A fair policy can promote a waiting patient after a defined waiting threshold, while never delaying immediately life-threatening cases. One example is to decrease the effective severity number by one after each fixed waiting interval, down to a configured maximum priority. The policy must be clinically approved; the software should not independently make medical decisions.

### 5. Deployment decision
A city hospital receiving 5,000 patients per day averages about 0.058 patients per second over 24 hours, although real arrivals are bursty. A PriorityQueue can handle this data volume efficiently in memory because insertion and removal are O(log n). A real deployment also needs persistence, multiple doctors/triage stations, audit logs, concurrency control, downtime recovery and clinical safety rules.

---

## Q7 – Day-4 Design Challenge: Diagnose, Redesign and Validate (13 Marks)

### Selected limitation
The Up Next playlist condition from Q2 is selected. In an ArrayList, repeatedly inserting at index 0 and removing index 0 shifts the remaining references. With n items, the total amount of shifting grows quadratically, O(n²), for a complete front-add/front-remove workload.

### A. Diagnose the bottleneck
The bottleneck is not object creation; it is repeated shifting of existing array elements during front insertion and removal. As the playlist grows, the number of shifted references increases. This is caused by ArrayList's contiguous backing array.

### B. Design objective
For a workload that only needs adding/removing at the front, reduce the sequence of front operations from O(n²) total shifting to O(n) total operations. Preserve the same order of items.

### C. Revised solution
Use `ArrayDeque<Integer>` as a double-ended queue. `addFirst()` and `removeFirst()` are amortized O(1). The deque uses a resizable circular array, so it avoids shifting all existing elements for each front operation. It does not support random indexed access, which is a trade-off.

### D. Prediction
For increasing n, ArrayList front-add/remove time should grow faster than ArrayDeque. The redesigned total operation count should be linear in n, while the original shifting work is quadratic in n.

### E. Implementation and validation
`Task07/PlaylistRedesignBenchmark.java` runs the same front-add/front-remove workload using ArrayList and ArrayDeque at sizes 1,000, 5,000 and 10,000.

Compile and run:
```bash
cd Task07
javac PlaylistRedesignBenchmark.java
java PlaylistRedesignBenchmark
```

Fill the actual results:

| n | ArrayList front add + remove (ns) | ArrayDeque front add + remove (ns) | Improvement ratio (ArrayList / Deque) |
|---:|---:|---:|---:|
| 1,000 | ___ | ___ | ___ |
| 5,000 | ___ | ___ | ___ |
| 10,000 | ___ | ___ | ___ |

### F. Final evaluation
The redesign is appropriate when operations are concentrated at the front or both ends. It is not a universal replacement for ArrayList: if the application frequently jumps to the k-th song, ArrayList remains better because indexed access is O(1), while ArrayDeque has no indexed access. The final choice depends on the measured operation mix.

---

## Overall Conclusion
This assignment compares string-building strategies, list and map implementations, hashing contracts, generic comparators and priority queues. The main engineering lesson is to choose a data structure based on the actual operations and ordering requirements, then validate the choice with repeatable measurements. Complexity analysis predicts growth, while local experiments show how the JVM and hardware affect practical performance.

## Submission checklist
- [ ] Create repository `CSA09-192473007-Assignment2`.
- [ ] Add this report and the `Task01`–`Task07` folders.
- [ ] Run every Java program on the laptop.
- [ ] Replace all measurement blanks with actual repeated measurements.
- [ ] Save genuine screenshots in the relevant `screenshots` folders.
- [ ] Add the final GitHub URL to this report and README.
- [ ] Make at least one commit per task, as required by the assignment.
- [ ] Submit the GitHub link and report to the faculty.
