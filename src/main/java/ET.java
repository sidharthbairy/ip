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
        String banner = " _____ _____\n"
                + "| ____|_   _|\n"
                + "|  _|   | |\n"
                + "| |___  | |\n"
                + "|_____| |_|";

        System.out.println("____________________________________________________________");
        System.out.println(banner);
        System.out.println("Hello, friend! I'm ET, a gentle visitor from far away.");
        System.out.println("I may be a little lost, but I would be happy to help with your tasks.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            CommandType commandType = CommandType.fromInput(command);
            System.out.println("____________________________________________________________");

            if (commandType == CommandType.BYE && command.equals("bye")) {
                System.out.println("     Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }

            if (commandType == CommandType.LIST && command.equals("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println("     " + (i + 1) + "." + tasks.get(i));
                }
            } else if (commandType == CommandType.MARK) {
                try {
                    int taskIndex = getTaskIndex(command, commandType, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("     Nice! I've marked this task as done:");
                    System.out.println("       " + tasks.get(taskIndex));
                } catch (ETException e) {
                    System.out.println("     " + e.getMessage());
                }
            } else if (commandType == CommandType.UNMARK) {
                try {
                    int taskIndex = getTaskIndex(command, commandType, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println("     OK, I've marked this task as not done yet:");
                    System.out.println("       " + tasks.get(taskIndex));
                } catch (ETException e) {
                    System.out.println("     " + e.getMessage());
                }
            } else if (commandType == CommandType.DELETE) {
                try {
                    int taskIndex = getTaskIndex(command, commandType, tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    System.out.println("     Noted. I've removed this task:");
                    System.out.println("       " + removedTask);
                    System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                } catch (ETException e) {
                    System.out.println("     " + e.getMessage());
                }
            } else {
                try {
                    Task task = parseTask(command, commandType);
                    tasks.add(task);
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + task);
                    System.out.println(
                            "     Now you have " + tasks.size() + " tasks in the list.");
                } catch (ETException e) {
                    System.out.println("     " + e.getMessage());
                }
            }

            System.out.println("____________________________________________________________");
        }
    }

    /**
     * Converts a command into a task. Date and time values are intentionally
     * kept as strings because ET does not need to compare or calculate them.
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
            return new Deadline(description, by);
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
            return new Event(description, from, to);
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
