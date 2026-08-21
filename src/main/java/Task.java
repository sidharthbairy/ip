/**
 * Represents the common state and behaviour of every task.
 *
 * <p>Specific task types inherit this class so that they can be stored in one
 * {@code Task[]} while adding their own display details.</p>
 */
public class Task {
    /** The text that describes this task. */
    protected final String description;

    /** Whether this task is currently marked as done. */
    private boolean isDone;

    /**
     * Creates a task that is not done yet.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }

        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the icon used to display this task's completion status.
     *
     * @return {@code X} when the task is done, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task's shared display portion.
     *
     * @return the status icon and task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
