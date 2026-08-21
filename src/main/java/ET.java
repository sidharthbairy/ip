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
            System.out.println("____________________________________________________________");

            if (command.equals("bye")) {
                System.out.println("     Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }

            if (command.equals("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println("     " + (i + 1) + "." + tasks.get(i));
                }
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                String taskNumber = command.substring("mark".length()).trim();
                try {
                    int taskIndex = Integer.parseInt(taskNumber) - 1;
                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        System.out.println("     That task number does not exist.");
                    } else {
                        tasks.get(taskIndex).markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + tasks.get(taskIndex));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("     Please specify a valid task number.");
                }
            } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                String taskNumber = command.substring("unmark".length()).trim();
                try {
                    int taskIndex = Integer.parseInt(taskNumber) - 1;
                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        System.out.println("     That task number does not exist.");
                    } else {
                        tasks.get(taskIndex).markAsNotDone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + tasks.get(taskIndex));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("     Please specify a valid task number.");
                }
            } else {
                try {
                    Task task = parseTask(command);
                    tasks.add(task);
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + task);
                    System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                } catch (IllegalArgumentException e) {
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
     * @return the task represented by the command
     * @throws IllegalArgumentException if a typed command is missing required information
     */
    private static Task parseTask(String command) {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring("todo".length()).trim();
            requireText(description, "Please provide a description for the ToDo.");
            return Task.todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String remainder = command.substring("deadline".length()).trim();
            int byMarker = remainder.indexOf("/by");
            if (byMarker < 0) {
                throw new IllegalArgumentException("A deadline must include a /by date or time.");
            }

            String description = remainder.substring(0, byMarker).trim();
            String by = remainder.substring(byMarker + "/by".length()).trim();
            requireText(description, "Please provide a description for the deadline.");
            requireText(by, "Please provide a date or time after /by.");
            return Task.deadline(description, by);
        }

        if (command.equals("event") || command.startsWith("event ")) {
            String remainder = command.substring("event".length()).trim();
            int fromMarker = remainder.indexOf("/from");
            int toMarker = fromMarker < 0
                    ? -1
                    : remainder.indexOf("/to", fromMarker + "/from".length());
            if (fromMarker < 0 || toMarker < 0) {
                throw new IllegalArgumentException(
                        "An event must include /from and /to date or time values.");
            }

            String description = remainder.substring(0, fromMarker).trim();
            String from = remainder.substring(fromMarker + "/from".length(), toMarker).trim();
            String to = remainder.substring(toMarker + "/to".length()).trim();
            requireText(description, "Please provide a description for the event.");
            requireText(from, "Please provide a starting date or time after /from.");
            requireText(to, "Please provide an ending date or time after /to.");
            return Task.event(description, from, to);
        }

        // Keep the original behaviour for unprefixed input: it is a ToDo.
        requireText(command, "Please enter a task.");
        return Task.todo(command);
    }

    /**
     * Ensures that a required command component is present.
     *
     * @param value the command component to check
     * @param errorMessage the message to use when it is missing
     */
    private static void requireText(String value, String errorMessage) {
        if (value.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
