/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    /** The text that describes this task. */
    protected String description;

    /** Whether this task is currently marked as done. */
    protected boolean isDone;

    /**
     * Creates a new task that is not done yet.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
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
     * Returns this task in the format used by the task list.
     *
     * @return the status icon and task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
