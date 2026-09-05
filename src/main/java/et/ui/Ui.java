package et.ui;

import et.task.Task;
import et.task.TaskList;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Handles ET's console input and output.
 *
 * <p>Keeping presentation code here lets the application logic describe what
 * happened without also needing to know how each message is formatted.</p>
 */
public class Ui {
    /** Separates consecutive console messages. */
    private static final String DIVIDER = "____________________________________________________________";

    /** Reads commands entered through the console without closing standard input. */
    private final Scanner scanner;

    /** Receives each complete message produced by ET. */
    private final Consumer<String> output;

    /** Creates a UI that reads from and writes to the console. */
    public Ui() {
        this(new Scanner(System.in), System.out::println);
    }

    /**
     * Creates an output-only UI for a graphical or test client.
     *
     * @param output the destination for each complete ET message
     */
    public Ui(Consumer<String> output) {
        this(null, output);
    }

    /** Creates a UI with the supplied input and output channels. */
    private Ui(Scanner scanner, Consumer<String> output) {
        this.scanner = scanner;
        this.output = output;
    }

    /**
     * Returns whether another complete command can be read from the console.
     *
     * @return {@code true} when another command is available
     */
    public boolean hasNextCommand() {
        assert scanner != null : "Console input must be available";
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next command entered by the user.
     *
     * @return the next command without leading or trailing whitespace
     */
    public String readCommand() {
        assert scanner != null : "Console input must be available";
        return scanner.nextLine().trim();
    }

    /** Displays ET's welcome message. */
    public void showWelcome() {
        String banner = " _____ _____\n"
                + "| ____|_   _|\n"
                + "|  _|   | |\n"
                + "| |___  | |\n"
                + "|_____| |_|";
        output.accept(DIVIDER + "\n" + banner + "\n" + getWelcomeMessage() + "\n" + DIVIDER);
    }

    /**
     * Returns the greeting shown at the start of a conversation.
     *
     * @return ET's greeting without console decoration
     */
    public String getWelcomeMessage() {
        return "Hello, friend! I'm ET, a gentle visitor from far away.\n"
                + "I may be a little lost, but I would be happy to help with your tasks.\n"
                + "What can I do for you?";
    }

    /** Displays a divider between command interactions. */
    public void showDivider() {
        output.accept(DIVIDER);
    }

    /** Displays ET's goodbye message. */
    public void showGoodbye() {
        output.accept("     Bye. Hope to see you again soon!");
    }

    /** Displays the tasks currently in the task list. */
    public void showTaskList(TaskList tasks) {
        StringBuilder message = new StringBuilder("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n     ").append(i + 1).append('.').append(tasks.getTask(i));
        }
        output.accept(message.toString());
    }

    /**
     * Displays the tasks whose descriptions matched a search keyword.
     *
     * @param matchingTasks the tasks to display in matching order
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        StringBuilder message = new StringBuilder("     Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            message.append("\n     ").append(i + 1).append('.').append(matchingTasks.get(i));
        }
        output.accept(message.toString());
    }

    /** Displays confirmation that a task was marked as completed. */
    public void showTaskMarked(Task task) {
        output.accept("     Nice! I've marked this task as done:\n       " + task);
    }

    /** Displays confirmation that a task was marked as incomplete. */
    public void showTaskUnmarked(Task task) {
        output.accept("     OK, I've marked this task as not done yet:\n       " + task);
    }

    /** Displays confirmation that a task was removed. */
    public void showTaskDeleted(Task task, int remainingTaskCount) {
        output.accept("     Noted. I've removed this task:\n       " + task
                + "\n     Now you have " + remainingTaskCount + " tasks in the list.");
    }

    /** Displays confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        output.accept("     Got it. I've added this task:\n       " + task
                + "\n     Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays a user-facing error message. */
    public void showError(String message) {
        output.accept("     " + message);
    }

    /** Explains that saved tasks could not be loaded. */
    public void showLoadingError() {
        showError("I couldn't load your saved tasks, so I'm starting with an empty list.");
    }

    /** Explains that a changed task list could not be saved. */
    public void showSavingError() {
        showError("Your task was changed, but I couldn't save it to disk.");
    }
}
