/**
 * Represents a task, its kind, and its optional date or time information.
 *
 * <p>The three task kinds are represented by data in this one class instead
 * of by a class hierarchy. This keeps the model simple while still allowing
 * each kind to be displayed differently.</p>
 */
public class Task {
    /** The kinds of tasks that ET can keep track of. */
    public enum Type {
        /** A task without any date or time attached to it. */
        TODO("T"),

        /** A task that must be completed by a particular date or time. */
        DEADLINE("D"),

        /** A task with a starting and ending date or time. */
        EVENT("E");

        /** The short label shown in the task list. */
        private final String displayName;

        Type(String displayName) {
            this.displayName = displayName;
        }

        /**
         * Returns the short label shown in the task list.
         *
         * @return {@code T}, {@code D}, or {@code E}
         */
        public String getDisplayName() {
            return displayName;
        }
    }

    /** The text that describes this task. */
    private final String description;

    /** Whether this task is currently marked as done. */
    private boolean isDone;

    /** The kind of this task. */
    private final Type type;

    /** The due date or time for a deadline, or {@code null} otherwise. */
    private final String by;

    /** The starting date or time for an event, or {@code null} otherwise. */
    private final String from;

    /** The ending date or time for an event, or {@code null} otherwise. */
    private final String to;

    /**
     * Creates a new ToDo task that is not done yet.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this(description, Type.TODO, null, null, null);
    }

    /**
     * Creates a task of the given kind that is not done yet.
     *
     * @param description the text describing the task
     * @param type the kind of task
     */
    public Task(String description, Type type) {
        this(description, type, null, null, null);
    }

    /**
     * Creates a deadline task that is not done yet.
     *
     * @param description the text describing the task
     * @param by the due date or time, kept as entered by the user
     */
    public Task(String description, String by) {
        this(description, Type.DEADLINE, by, null, null);
    }

    /**
     * Creates a task with all of its optional date or time information.
     *
     * @param description the text describing the task
     * @param type the kind of task
     * @param by the deadline date or time, if this is a deadline
     * @param from the event start date or time, if this is an event
     * @param to the event end date or time, if this is an event
     */
    public Task(String description, Type type, String by, String from, String to) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Task type cannot be null.");
        }

        this.description = description;
        this.isDone = false;
        this.type = type;
        this.by = by;
        this.from = from;
        this.to = to;
    }

    /**
     * Creates a ToDo task.
     *
     * @param description the text describing the task
     * @return a new ToDo task
     */
    public static Task todo(String description) {
        return new Task(description, Type.TODO);
    }

    /**
     * Creates a deadline task.
     *
     * @param description the text describing the task
     * @param by the due date or time
     * @return a new deadline task
     */
    public static Task deadline(String description, String by) {
        return new Task(description, Type.DEADLINE, by, null, null);
    }

    /**
     * Creates an event task.
     *
     * @param description the text describing the task
     * @param from the event start date or time
     * @param to the event end date or time
     * @return a new event task
     */
    public static Task event(String description, String from, String to) {
        return new Task(description, Type.EVENT, null, from, to);
    }

    /**
     * Returns this task's kind.
     *
     * @return this task's kind
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the deadline date or time.
     *
     * @return the deadline, or {@code null} for a ToDo or event
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns the starting date or time of an event.
     *
     * @return the event start, or {@code null} when this is not an event
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the ending date or time of an event.
     *
     * @return the event end, or {@code null} when this is not an event
     */
    public String getTo() {
        return to;
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
     * @return the type, status, description, and any date or time information
     */
    @Override
    public String toString() {
        String taskText = "[" + type.getDisplayName() + "][" + getStatusIcon() + "] " + description;
        if (type == Type.DEADLINE) {
            return taskText + " (by: " + by + ")";
        }
        if (type == Type.EVENT) {
            return taskText + " (from: " + from + " to: " + to + ")";
        }
        return taskText;
    }
}
