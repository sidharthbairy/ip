/**
 * Handles all messages displayed by ET in the console.
 *
 * <p>Keeping presentation code here lets the application logic describe what
 * happened without also needing to know how each message is formatted.</p>
 */
public class Ui {
    /** Separates consecutive console messages. */
    private static final String DIVIDER = "____________________________________________________________";

    /** Displays ET's welcome message. */
    public void showWelcome() {
        String banner = " _____ _____\n"
                + "| ____|_   _|\n"
                + "|  _|   | |\n"
                + "| |___  | |\n"
                + "|_____| |_|";
        System.out.println(DIVIDER);
        System.out.println(banner);
        System.out.println("Hello, friend! I'm ET, a gentle visitor from far away.");
        System.out.println("I may be a little lost, but I would be happy to help with your tasks.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /** Displays a divider between command interactions. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Displays ET's goodbye message. */
    public void showGoodbye() {
        System.out.println("     Bye. Hope to see you again soon!");
    }

    /** Displays the tasks currently in the task list. */
    public void showTaskList(java.util.List<Task> tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays confirmation that a task was marked as completed. */
    public void showTaskMarked(Task task) {
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println("       " + task);
    }

    /** Displays confirmation that a task was marked as incomplete. */
    public void showTaskUnmarked(Task task) {
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       " + task);
    }

    /** Displays confirmation that a task was removed. */
    public void showTaskDeleted(Task task, int remainingTaskCount) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + remainingTaskCount + " tasks in the list.");
    }

    /** Displays confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays a user-facing error message. */
    public void showError(String message) {
        System.out.println("     " + message);
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
