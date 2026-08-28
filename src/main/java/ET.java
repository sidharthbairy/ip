import java.io.IOException;
import java.util.Optional;

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

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            CommandType commandType = CommandType.fromInput(command);
            ui.showDivider();

            Optional<Command> simpleCommand = parser.parseSimpleCommand(command);
            if (simpleCommand.isPresent()) {
                Command parsedCommand = simpleCommand.get();
                parsedCommand.execute(tasks, ui, storage);
                if (parsedCommand.isExit()) {
                    ui.showDivider();
                    break;
                }
            } else if (commandType == CommandType.MARK) {
                try {
                    Command taskStatusCommand = parser.parseTaskStatusCommand(command, commandType, tasks.size());
                    taskStatusCommand.execute(tasks, ui, storage);
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.UNMARK) {
                try {
                    Command taskStatusCommand = parser.parseTaskStatusCommand(command, commandType, tasks.size());
                    taskStatusCommand.execute(tasks, ui, storage);
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    Command deleteCommand = parser.parseDeleteCommand(command, tasks.size());
                    deleteCommand.execute(tasks, ui, storage);
                } catch (ETException e) {
                    ui.showError(e.getMessage());
                }
            } else {
                try {
                    Command addCommand = parser.parseAddCommand(command, commandType);
                    addCommand.execute(tasks, ui, storage);
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

}
