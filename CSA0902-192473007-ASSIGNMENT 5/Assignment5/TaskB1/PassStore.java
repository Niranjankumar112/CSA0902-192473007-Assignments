import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PassStore {
    private final Path file;
    public PassStore(Path file) { this.file = file; }

    public void add(String passId, String regNo, String name, int route, double amount) throws IOException {
        // The sample format uses comma delimiter; fields containing commas are rejected for clarity.
        if (passId.contains(",") || regNo.contains(",") || name.contains(","))
            throw new IllegalArgumentException("Comma is not allowed in fields in this simple CSV exercise.");
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(String.join(",", passId, regNo, name, String.valueOf(route), String.valueOf(amount)));
            w.newLine();
        }
    }

    /** Returns five fields for matching pass ID; skips damaged lines. */
    public String[] find(String passId) throws IOException {
        // TODO 1 completed: buffered scan, try-with-resources, malformed rows skipped.
        if (!Files.exists(file)) return null;
        try (BufferedReader r = Files.newBufferedReader(file)) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length != 5) continue;
                try {
                    Integer.parseInt(fields[3]);
                    Double.parseDouble(fields[4]);
                } catch (NumberFormatException damaged) {
                    continue;
                }
                if (fields[0].equals(passId)) return fields;
            }
        }
        return null;
    }

    public List<String[]> listByRoute(int route) throws IOException {
        List<String[]> found = new ArrayList<>();
        if (!Files.exists(file)) return found;
        try (BufferedReader r = Files.newBufferedReader(file)) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] f = line.split(",", -1);
                if (f.length != 5) continue;
                try {
                    if (Integer.parseInt(f[3]) == route) {
                        Double.parseDouble(f[4]);
                        found.add(f);
                    }
                } catch (NumberFormatException ignored) { }
            }
        }
        return found;
    }

    public static void main(String[] args) throws Exception {
        Path f = Files.createTempFile("passes", ".csv");
        PassStore store = new PassStore(f);
        store.add("P1001", "192311001", "Arun", 12, 4500);
        Files.writeString(f, "P1002,DAMAGED LINE\n", StandardOpenOption.APPEND);
        store.add("P1003", "192311003", "Divya", 7, 3800);
        System.out.println(Arrays.toString(store.find("P1003")));
        System.out.println(Arrays.toString(store.find("P1002")));
        Files.deleteIfExists(f);
    }
}
