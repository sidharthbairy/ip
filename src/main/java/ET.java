import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the ET task companion.
 */
public class ET {
    /** Prevents instantiation of this entry-point class. */
    private ET() {
    }

    /**
     * Starts ET and processes commands from standard input.
     *
     * @param args command-line arguments, which ET does not currently use
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        Storage storage = new Storage();
        List<Task> tasks = loadTasks(storage, ui);
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            CommandType commandType = CommandType.fromInput(command);
            ui.showDivider();

            if (commandType == CommandType.BYE && command.equals("bye")) {
                ui.showGoodbye();
                ui.showDivider();
                break;
            }

            if (commandType == CommandType.LIST && command.equals("list")) {
                ui.showTaskList(tasks);
            } else if (commandType == CommandType.MARK) {
                try {
                    int taskIndex = getTaskIndex(command, commandType, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    saveTasks(storage, tasks, ui);
                    ui.showTaskMarked(tasks.get(taskIndex));
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskIndex = getTaskIndex(command, commandType, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    saveTasks(storage, tasks, ui);
                    ui.showTaskUnmarked(tasks.get(taskIndex));
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskIndex = getTaskIndex(command, commandType, tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    saveTasks(storage, tasks, ui);
                    ui.showTaskDeleted(removedTask, tasks.size());
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            } else {
                try {
                    Task task = parseTask(command, commandType);
                    tasks.add(task);
                    saveTasks(storage, tasks, ui);
                    ui.showTaskAdded(task, tasks.size());
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            }

            ui.showDivider();
        }
    }

    /**
     * Loads saved tasks while allowing ET to continue when the storage file is unavailable.
     *
     * @param storage the component that reads ET's task file
     * @return the loaded tasks, or an empty list when reading fails
     */
    private static List<Task> loadTasks(Storage storage, Ui ui) {
        try {
            return storage.load();
        } catch (IOException e) {
            ui.showLoadingError();
            return new ArrayList<>();
        }
    }

    /**
     * Saves the task list after a command changes it.
     *
     * @param storage the component that writes ET's task file
     * @param tasks the current tasks to save
     */
    private static void saveTasks(Storage storage, List<Task> tasks, Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            ui.showSavingError();
        }
    }

    /**
     * Converts a command into a task, including parsing date and time input.
     *
     * @param command the command entered by the user
     * @param commandType the recognised type of the command
     * @return the task represented by the command
     * @throws ETException if the command is unknown or missing required information
     */
    private static Task parseTask(String command, CommandType commandType) throws ETException {
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
     * Ensures that a required command component is present.
     *
     * @param value the command component to check
     * @param errorMessage the message to use when it is missing
     */
    private static void requireText(String value, String errorMessage) throws ETException {
        if (value.isBlank()) {
            throw new ETException(errorMessage);
        }
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
    private static int getTaskIndex(String command, CommandType commandType, int taskCount)
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
}
