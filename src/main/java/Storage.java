import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Saves ET's task list to, and loads it from, a file below the project root.
 *
 * <p>The format has one task per line. Text fields are Base64 encoded so that
 * task descriptions can safely contain the separator used between fields.</p>
 */
public class Storage {
    /** The portable, project-relative location of ET's saved tasks. */
    private static final Path STORAGE_FILE = Path.of("data", "tasks.txt");

    /** Separates the simple fields in a saved task line. */
    private static final String FIELD_SEPARATOR = " | ";

    /**
     * Loads every valid task in the storage file.
     *
     * <p>A missing file means that ET is being used for the first time, so an
     * empty list is returned. Malformed lines are skipped to let ET recover
     * from a partially corrupted storage file.</p>
     *
     * @return the valid tasks that were read from disk
     * @throws IOException if the storage file cannot be read
     */
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (Files.notExists(STORAGE_FILE)) {
            return tasks;
        }

        for (String line : Files.readAllLines(STORAGE_FILE, StandardCharsets.UTF_8)) {
            try {
                tasks.add(parseTask(line));
            } catch (IllegalArgumentException e) {
                // Skip only the corrupted line; the remaining saved tasks are still useful.
            }
        }
        return tasks;
    }

    /**
     * Replaces the storage file with the current task list.
     *
     * @param tasks the tasks to save
     * @throws IOException if ET cannot create the data directory or write the file
     */
    public void save(List<Task> tasks) throws IOException {
        Files.createDirectories(STORAGE_FILE.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(STORAGE_FILE, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Task task : tasks) {
                writer.write(serializeTask(task));
                writer.newLine();
            }
        }
    }

    /**
     * Converts one storage line into its matching task object.
     *
     * @param line one line from the storage file
     * @return the reconstructed task
     * @throws IllegalArgumentException if the line is not in ET's storage format
     */
    private Task parseTask(String line) {
        String[] fields = line.split(" \\| ", -1);
        if (fields.length < 3 || !fields[1].matches("[01]")) {
            throw new IllegalArgumentException("Invalid saved task");
        }

        Task task;
        switch (fields[0]) {
        case "T":
            requireFieldCount(fields, 3);
            task = new Todo(decode(fields[2]));
            break;
        case "D":
            requireFieldCount(fields, 4);
            task = new Deadline(decode(fields[2]), decode(fields[3]));
            break;
        case "E":
            requireFieldCount(fields, 5);
            task = new Event(decode(fields[2]), decode(fields[3]), decode(fields[4]));
            break;
        default:
            throw new IllegalArgumentException("Unknown saved task type");
        }

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Verifies that a saved line contains exactly the required number of fields.
     *
     * @param fields the fields from a saved line
     * @param expectedCount the required number of fields
     * @throws IllegalArgumentException if the field count does not match
     */
    private void requireFieldCount(String[] fields, int expectedCount) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException("Invalid saved task field count");
        }
    }

    /**
     * Serializes one task to its one-line storage representation.
     *
     * @param task the task to serialize
     * @return the corresponding storage line
     */
    private String serializeTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        switch (task.getTaskType()) {
        case TODO:
            return String.join(FIELD_SEPARATOR, "T", status, encode(task.getDescription()));
        case DEADLINE:
            Deadline deadline = (Deadline) task;
            return String.join(FIELD_SEPARATOR, "D", status, encode(deadline.getDescription()),
                    encode(deadline.getBy()));
        case EVENT:
            Event event = (Event) task;
            return String.join(FIELD_SEPARATOR, "E", status, encode(event.getDescription()),
                    encode(event.getFrom()), encode(event.getTo()));
        default:
            throw new IllegalArgumentException("Unknown task type");
        }
    }

    /**
     * Encodes a text field so it cannot interfere with the storage separator.
     *
     * @param value the text to encode
     * @return a Base64 representation of the text
     */
    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes one text field from the storage file.
     *
     * @param value the Base64 field to decode
     * @return the original text
     * @throws IllegalArgumentException if the field is not valid Base64 text
     */
    private String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
