import java.util.Scanner;

/**
 * Entry point for the ET task companion.
 */
public class ET {
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
        String[] tasks = new String[100];
        boolean[] taskDone = new boolean[100];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println("____________________________________________________________");

            if (command.equals("bye")) {
                System.out.println("     Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }

            if (command.equals("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    String status = taskDone[i] ? "X" : " ";
                    System.out.println("     " + (i + 1) + ".[" + status + "] " + tasks[i]);
                }
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                String taskNumber = command.substring("mark".length()).trim();
                try {
                    int taskIndex = Integer.parseInt(taskNumber) - 1;
                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println("     That task number does not exist.");
                    } else {
                        taskDone[taskIndex] = true;
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       [X] " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("     Please specify a valid task number.");
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("     added: " + command);
            }

            System.out.println("____________________________________________________________");
        }
    }
}
