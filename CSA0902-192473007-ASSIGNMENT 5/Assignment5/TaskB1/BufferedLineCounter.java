import java.io.*;
import java.nio.file.*;

public class BufferedLineCounter {
    public static long count(Path path) throws IOException {
        long lines = 0; boolean hasChars = false, lastWasNewline = true;
        try (BufferedReader r = Files.newBufferedReader(path)) {
            int ch;
            while ((ch = r.read()) != -1) {
                hasChars = true;
                if (ch == '\\n') lines++;
                lastWasNewline = ch == '\\n';
            }
        }
        return lines + (hasChars && !lastWasNewline ? 1 : 0);
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) { System.out.println("Usage: java BufferedLineCounter <file>"); return; }
        long start = System.nanoTime();
        System.out.println("Lines: " + count(Path.of(args[0])));
        System.out.println("Elapsed ms: " + ((System.nanoTime()-start)/1_000_000.0));
    }
}
