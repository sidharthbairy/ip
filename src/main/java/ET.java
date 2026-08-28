import java.io.IOException;
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
        Parser parser = new Parser();
        TaskList tasks = loadTasks(storage, ui);
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
                    int taskIndex = parser.parseTaskIndex(command, commandType, tasks.size());
                    tasks.getTask(taskIndex).markAsDone();
                    saveTasks(storage, tasks, ui);
                    ui.showTaskMarked(tasks.getTask(taskIndex));
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskIndex = parser.parseTaskIndex(command, commandType, tasks.size());
                    tasks.getTask(taskIndex).markAsNotDone();
                    saveTasks(storage, tasks, ui);
                    ui.showTaskUnmarked(tasks.getTask(taskIndex));
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskIndex = parser.parseTaskIndex(command, commandType, tasks.size());
                    Task removedTask = tasks.removeTask(taskIndex);
                    saveTasks(storage, tasks, ui);
                    ui.showTaskDeleted(removedTask, tasks.size());
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            } else {
                try {
                    Task task = parser.parseTask(command, commandType);
                    tasks.addTask(task);
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
    private static TaskList loadTasks(Storage storage, Ui ui) {
        try {
            return new TaskList(storage.load());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /**
     * Saves the task list after a command changes it.
     *
     * @param storage the component that writes ET's task file
     * @param tasks the current tasks to save
     */
    private static void saveTasks(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showSavingError();
        }
    }

}
