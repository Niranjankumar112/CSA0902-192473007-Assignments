# A2 – Don’t Read What You Don’t Need (5 marks)
A 50 MiB file contains 52,428,800 bytes, so a one-byte-at-a-time loop makes approximately 52.4 million Java `read()` calls. Each call has method/stream overhead; buffering amortizes this by reading chunks (for example 8 KiB), reducing underlying read operations to roughly fileSize/bufferSize, while the loop still processes every byte.

Use `BufferedInputStream` with an 8 KiB or 64 KiB buffer, or `Files.newBufferedReader` for text. Count `\n` bytes and handle a final line without a newline if counting logical lines. Buffering may help little when the data is already in memory/cache or the workload is CPU-bound and performs very little I/O.

Alternative `Files.readAllLines()` reads and stores all lines: O(file size) time and O(file size + line-object overhead) memory. Buffered streaming is O(file size) time and O(buffer size) extra memory. Use readAllLines for small files that fit comfortably in memory and need repeated random access; use streaming for large logs.
