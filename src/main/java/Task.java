/**
 * Represents the common state and behaviour of every task.
 *
 * <p>Specific task types inherit this class so that they can be stored in one
 * {@code List<Task>} while adding their own display details.</p>
 */
public class Task {
    /** The category of this task. */
    private final TaskType taskType;

    /** The text that describes this task. */
    protected final String description;

    /** Whether this task is currently marked as done. */
    private boolean isDone;

    /**
     * Creates a task that is not done yet.
     *
     * @param taskType the category of task being created
     * @param description the text describing the task
     */
    public Task(TaskType taskType, String description) {
        if (taskType == null) {
            throw new IllegalArgumentException("Task type cannot be null.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }

        this.taskType = taskType;
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
     * @return the task type, status icon, and task description
     */
    @Override
    public String toString() {
        return "[" + taskType.getDisplayCode() + "][" + getStatusIcon() + "] " + description;
    }
}
