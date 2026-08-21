/**
 * Represents a task without any date or time information.
 */
public class Todo extends Task {
    /**
     * Creates a ToDo task that is not done yet.
     *
     * @param description the text describing the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this ToDo in the format used by the task list.
     *
     * @return the ToDo type, status icon, and description
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
