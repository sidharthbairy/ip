import java.io.IOException;

/**
 * Entry point for the ET task companion.
 */
public class ET {
    /** The component that handles console input and output. */
    private final Ui ui;

    /** The component that loads and saves the task list. */
    private final Storage storage;

    /** The component that converts user input into commands. */
    private final Parser parser;

    /** The tasks managed during the current ET session. */
    private TaskList tasks;

    /** Creates ET's collaborating components. */
    public ET() {
        ui = new Ui();
        storage = new Storage();
        parser = new Parser();
    }

    /**
     * Starts ET and processes commands from standard input until an exit command is run.
     */
    public void run() {
        ui.showWelcome();
        tasks = loadTasks();
        boolean isExit = false;

        while (!isExit && ui.hasNextCommand()) {
            try {
                String fullCommand = ui.readCommand();
                ui.showDivider();
                Command command = parser.parseCommand(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (ETException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showDivider();
            }
        }
    }

    /**
     * Loads saved tasks while allowing ET to continue when the storage file is unavailable.
     *
     * @return the loaded tasks, or an empty list when reading fails
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /** Starts the ET application. */
    public static void main(String[] args) {
        new ET().run();
    }
}
