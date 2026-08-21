/**
 * Represents a task that must be completed by a given date or time.
 */
public class Deadline extends Task {
    /** The due date or time, kept as entered by the user. */
    private final String by;

    /**
     * Creates a deadline task that is not done yet.
     *
     * @param description the text describing the task
     * @param by the due date or time
     */
    public Deadline(String description, String by) {
        super(TaskType.DEADLINE, description);
        if (by == null || by.isBlank()) {
            throw new IllegalArgumentException("Deadline date or time cannot be empty.");
        }
        this.by = by;
    }

    /**
     * Returns this deadline in the format used by the task list.
     *
     * @return the deadline type, status icon, description, and due date or time
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
