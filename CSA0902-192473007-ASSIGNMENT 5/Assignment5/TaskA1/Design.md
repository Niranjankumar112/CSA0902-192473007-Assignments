# A1 – Design a File Format (7 marks)

## Delimited text record
Field order: `passId|regNo|name|routeNo|boardingStop|validityDate|amountPaid`
- UTF-8, one record per line; date uses ISO `yyyy-MM-dd`; amount uses decimal notation.
- Escape `\` as `\\` and `|` as `\|`; reject newline in fields or encode it as `\n`. This prevents delimiter ambiguity.
- Append with `BufferedWriter`; scan with `BufferedReader`. Validate field count, route integer, date and amount.

## Binary DataOutputStream record
Use `DataOutputStream` over `BufferedOutputStream`:
`writeUTF(passId), writeUTF(regNo), writeUTF(name), writeInt(routeNo), writeUTF(boardingStop), writeUTF(validityDate), writeDouble(amountPaid)`.
Each variable-length UTF field is length-prefixed by writeUTF; primitive values have fixed binary representations. Document a format version/magic header.

## SIMATS-style pseudocode
```
ALGORITHM AddPass
INPUT pass
BEGIN
 VALIDATE all fields in pass
 OPEN BufferedWriter in APPEND mode
 WRITE escaped fields in the defined order and a newline
 CLOSE writer
END
```
```
ALGORITHM FindPass
INPUT targetPassId
BEGIN
 IF file does not exist THEN RETURN NOT_FOUND
 OPEN BufferedReader
 WHILE a line can be read DO
  SPLIT line using escaped-delimiter parser
  IF field count is not 7 THEN LOG damaged line; CONTINUE
  IF first field equals targetPassId THEN RETURN parsed record
 END WHILE
 RETURN NOT_FOUND
END
```
```
ALGORITHM ListByRoute
INPUT targetRoute
BEGIN
 IF file does not exist THEN RETURN empty list
 OPEN BufferedReader
 WHILE a line can be read DO
  PARSE and validate record
  IF damaged THEN LOG and CONTINUE
  IF route number equals targetRoute THEN ADD record to result
 END WHILE
 RETURN result
END
```

## Damaged-data strategy
Missing file: distinguish first-time creation from unexpected deletion; return a clear status and create only for add. Damaged line: validate field count/types/date, log line number and skip/quarantine it. Power loss during append: a final partial line is rejected; write updates to a temporary file, flush/sync, then atomically rename where supported. For critical records, use database transactions and backups.

## Alternative: fixed-length RandomAccessFile
If record index is known, seek to `i × recordSize` and read in O(1); finding by pass ID still needs O(n) unless a separate index maps ID to record position. Fixed-width name/stop fields require padding and can waste space; text records are variable-length and human-readable.
