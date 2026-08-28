package et.parser;

import et.command.AddCommand;
import et.command.Command;
import et.command.DeleteCommand;
import et.command.ExitCommand;
import et.command.ListCommand;
import et.command.MarkCommand;
import et.command.UnmarkCommand;
import et.exception.ETException;
import et.task.Deadline;
import et.task.Event;
import et.task.Task;
import et.task.Todo;

/**
 * Interprets user commands and converts their task-related parts into objects.
 */
public class Parser {
    /**
     * Converts one full user command into the command object that will execute it.
     *
     * @param command the command entered by the user
     * @return the matching command object
     * @throws ETException if the command is unknown or missing required information
     */
    public Command parseCommand(String command) throws ETException {
        CommandType commandType = CommandType.fromInput(command);
        switch (commandType) {
        case TODO:
        case DEADLINE:
        case EVENT:
            return new AddCommand(parseTask(command, commandType));
        case LIST:
            if (command.equals(CommandType.LIST.getKeyword())) {
                return new ListCommand();
            }
            break;
        case MARK:
            return new MarkCommand(parseTaskIndex(command, commandType));
        case UNMARK:
            return new UnmarkCommand(parseTaskIndex(command, commandType));
        case DELETE:
            return new DeleteCommand(parseTaskIndex(command, commandType));
        case BYE:
            if (command.equals(CommandType.BYE.getKeyword())) {
                return new ExitCommand();
            }
            break;
        default:
            break;
        }
        throw new ETException("I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /**
     * Converts a task-creation command into a task, including its date and time input.
     *
     * @param command the command entered by the user
     * @param commandType the recognised type of the command
     * @return the task represented by the command
     * @throws ETException if the command is unknown or missing required information
     */
    private Task parseTask(String command, CommandType commandType) throws ETException {
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

        throw new IllegalArgumentException("Unsupported task command");
    }

    /**
     * Extracts the one-based task number used by task commands.
     *
     * @param command the full command entered by the user
     * @param commandType the command that accepts a task number
     * @return the zero-based index of the referenced task
     * @throws ETException if the task number is absent or invalid
     */
    private int parseTaskIndex(String command, CommandType commandType) throws ETException {
        String taskNumber = command.substring(commandType.getKeyword().length()).trim();
        try {
            return Integer.parseInt(taskNumber) - 1;
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
