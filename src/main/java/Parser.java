import java.util.Optional;

/**
 * Interprets user commands and converts their task-related parts into objects.
 */
public class Parser {
    /**
     * Converts a command with no arguments into its command object.
     *
     * <p>Commands that require arguments are still handled by ET during this
     * incremental migration and therefore return an empty result.</p>
     *
     * @param command the command entered by the user
     * @return the matching simple command, or an empty result when it needs further parsing
     */
    public Optional<Command> parseSimpleCommand(String command) {
        if (command.equals(CommandType.LIST.getKeyword())) {
            return Optional.of(new ListCommand());
        }
        if (command.equals(CommandType.BYE.getKeyword())) {
            return Optional.of(new ExitCommand());
        }
        return Optional.empty();
    }

    /**
     * Converts a mark or unmark command into its command object.
     *
     * @param command the command entered by the user
     * @param commandType the recognised mark or unmark command type
     * @param taskCount the number of tasks currently stored
     * @return the matching task-status command
     * @throws ETException if the task number is absent, invalid, or out of range
     */
    public Command parseTaskStatusCommand(String command, CommandType commandType, int taskCount)
            throws ETException {
        int taskIndex = parseTaskIndex(command, commandType, taskCount);
        if (commandType == CommandType.MARK) {
            return new MarkCommand(taskIndex);
        }
        return new UnmarkCommand(taskIndex);
    }

    /**
     * Converts a task-creation command into a task, including its date and time input.
     *
     * @param command the command entered by the user
     * @param commandType the recognised type of the command
     * @return the task represented by the command
     * @throws ETException if the command is unknown or missing required information
     */
    public Task parseTask(String command, CommandType commandType) throws ETException {
        if (commandType == CommandType.TODO) {
            String description = command.substring(commandType.getKeyword().length()).trim();
            requireText(description, "Please provide a description for the ToDo.");
            return new Todo(description);
        }

        if (commandType == CommandType.DEADLINE) {
            String remainder = command.substring(commandType.getKeyword().length()).trim();
            int byMarker = remainder.indexOf("/by");
            if (byMarker < 0) {
                throw new ETException("Please include /by followed by the deadline date or time.");
            }

            String description = remainder.substring(0, byMarker).trim();
            String by = remainder.substring(byMarker + "/by".length()).trim();
            requireText(description, "Please provide a description for the deadline.");
            requireText(by, "Please provide a date or time after /by.");
            DateTimeParser.ParsedDateTime deadlineDate = DateTimeParser.parse(by);
            return new Deadline(description, deadlineDate.value(), deadlineDate.hasTime());
        }

        if (commandType == CommandType.EVENT) {
            String remainder = command.substring(commandType.getKeyword().length()).trim();
            int fromMarker = remainder.indexOf("/from");
            int toMarker = fromMarker < 0
                    ? -1
                    : remainder.indexOf("/to", fromMarker + "/from".length());
            if (fromMarker < 0 || toMarker < 0) {
                throw new ETException("Please include both /from and /to for the event time.");
            }

            String description = remainder.substring(0, fromMarker).trim();
            String from = remainder.substring(fromMarker + "/from".length(), toMarker).trim();
            String to = remainder.substring(toMarker + "/to".length()).trim();
            requireText(description, "Please provide a description for the event.");
            requireText(from, "Please provide a starting date or time after /from.");
            requireText(to, "Please provide an ending date or time after /to.");
            DateTimeParser.ParsedDateTime startDate = DateTimeParser.parse(from);
            DateTimeParser.ParsedDateTime endDate = DateTimeParser.parse(to);
            if (endDate.value().isBefore(startDate.value())) {
                throw new ETException("The event end date and time cannot be before its start.");
            }
            return new Event(description, startDate.value(), startDate.hasTime(),
                    endDate.value(), endDate.hasTime());
        }

        throw new ETException("I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /**
     * Extracts and validates the one-based task number used by task commands.
     *
     * @param command the full command entered by the user
     * @param commandType the command that accepts a task number
     * @param taskCount the number of tasks currently stored
     * @return the zero-based index of the referenced task
     * @throws ETException if the task number is absent, invalid, or out of range
     */
    public int parseTaskIndex(String command, CommandType commandType, int taskCount)
            throws ETException {
        String taskNumber = command.substring(commandType.getKeyword().length()).trim();
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                throw new ETException("That task number is not in the current list.");
            }
            return taskIndex;
        } catch (NumberFormatException e) {
            throw new ETException("Please give a valid task number after " + commandType.getKeyword() + ".");
        }
    }

    /**
     * Ensures that a required command component is present.
     *
     * @param value the command component to check
     * @param errorMessage the message to use when it is missing
     * @throws ETException if the command component is blank
     */
    private void requireText(String value, String errorMessage) throws ETException {
        if (value.isBlank()) {
            throw new ETException(errorMessage);
        }
    }
}
