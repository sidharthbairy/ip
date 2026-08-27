/**
 * Represents a task with a start and end date or time.
 */
public class Event extends Task {
    /** The starting date or time, kept as entered by the user. */
    private final String from;

    /** The ending date or time, kept as entered by the user. */
    private final String to;

    /**
     * Creates an event task that is not done yet.
     *
     * @param description the text describing the task
     * @param from the event start date or time
     * @param to the event end date or time
     */
    public Event(String description, String from, String to) {
        super(TaskType.EVENT, description);
        if (from == null || from.isBlank()) {
            throw new IllegalArgumentException("Event start date or time cannot be empty.");
        }
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Event end date or time cannot be empty.");
        }
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event's start date or time.
     *
     * @return the date or time entered after {@code /from}
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns this event's end date or time.
     *
     * @return the date or time entered after {@code /to}
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns this event in the format used by the task list.
     *
     * @return the event type, status icon, description, and time range
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
