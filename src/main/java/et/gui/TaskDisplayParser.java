package et.gui;

import et.task.TaskType;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts task details from ET response lines for graphical rendering.
 */
final class TaskDisplayParser {
    /** Matches an optional list number followed by ET's task type, status, and description. */
    private static final Pattern TASK_LINE_PATTERN = Pattern.compile(
            "^\\s*(?:(\\d+)\\.)?\\[([TDE])\\]\\[([ X✓])\\]\\s+(.+)$");

    /** Prevents instantiation of this utility class. */
    private TaskDisplayParser() {
    }

    /**
     * Parses one response line when it contains a task.
     *
     * @param line one line from ET's response
     * @return the task display details, or an empty value for ordinary message text
     */
    static Optional<TaskDisplay> parseTaskLine(String line) {
        Matcher matcher = TASK_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String taskNumber = matcher.group(1) == null ? "" : matcher.group(1);
        TaskType taskType = parseTaskType(matcher.group(2));
        boolean isDone = !matcher.group(3).isBlank();
        return Optional.of(new TaskDisplay(taskNumber, taskType, isDone, matcher.group(4)));
    }

    /**
     * Converts a task's persisted display code into its task type.
     *
     * @param displayCode the single-letter task display code
     * @return the corresponding task type
     */
    private static TaskType parseTaskType(String displayCode) {
        return switch (displayCode) {
        case "T" -> TaskType.TODO;
        case "D" -> TaskType.DEADLINE;
        case "E" -> TaskType.EVENT;
        default -> throw new IllegalArgumentException("Unknown task display code");
        };
    }

    /**
     * Contains the task details needed to build a graphical task card.
     *
     * @param taskNumber the optional one-based list position
     * @param taskType the task's category
     * @param isDone whether the task is complete
     * @param description the task description and any date details
     */
    record TaskDisplay(String taskNumber, TaskType taskType, boolean isDone, String description) {
    }
}
