import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Nori task-tracking chatbot.
 */
public class Nori {
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println("Hello! I'm Nori.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.equals("bye")) {
                break;
            }
            try {
                handle(input, tasks);
            } catch (NoriException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(LINE);
        }

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Parses and executes one user command against the current task list.
     */
    private static void handle(String input, List<Task> tasks) throws NoriException {
        String[] commandParts = input.split("\\s+", 2);
        String command = commandParts[0];
        String arguments = commandParts.length == 2 ? commandParts[1].trim() : "";

        switch (command) {
        case "list":
            requireNoArguments(arguments, "list");
            showTasks(tasks);
            break;
        case "mark":
            Task markedTask = tasks.get(parseTaskIndex(arguments, tasks));
            markedTask.mark();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + markedTask);
            break;
        case "unmark":
            Task unmarkedTask = tasks.get(parseTaskIndex(arguments, tasks));
            unmarkedTask.unmark();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + unmarkedTask);
            break;
        case "delete":
            Task deletedTask = tasks.remove(parseTaskIndex(arguments, tasks));
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + deletedTask);
            printTaskCount(tasks);
            break;
        case "todo":
            requireDescription(arguments, "todo");
            addTask(tasks, new Todo(arguments));
            break;
        case "deadline":
            String[] deadlineParts = splitAround(arguments, " /by ", "deadline DESCRIPTION /by TIME");
            addTask(tasks, new Deadline(deadlineParts[0], deadlineParts[1]));
            break;
        case "event":
            String[] fromParts = splitAround(arguments, " /from ", "event DESCRIPTION /from START /to END");
            String[] toParts = splitAround(fromParts[1], " /to ", "event DESCRIPTION /from START /to END");
            addTask(tasks, new Event(fromParts[0], toParts[0], toParts[1]));
            break;
        default:
            throw new NoriException("I don't recognise that command. "
                    + "Try todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
    }

    private static void showTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty.");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Converts a one-based user task number to a valid zero-based list index.
     */
    private static int parseTaskIndex(String argument, List<Task> tasks) throws NoriException {
        if (argument.isEmpty()) {
            throw new NoriException("Please provide a task number.");
        }
        try {
            int index = Integer.parseInt(argument) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new NoriException("That task number is not in the list.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new NoriException("The task number must be a whole number.");
        }
    }

    /**
     * Splits arguments around a required marker and verifies both resulting values.
     */
    private static String[] splitAround(String arguments, String marker, String usage) throws NoriException {
        int markerIndex = arguments.indexOf(marker);
        if (markerIndex < 0) {
            throw new NoriException("Please use this format: " + usage);
        }
        String before = arguments.substring(0, markerIndex).trim();
        String after = arguments.substring(markerIndex + marker.length()).trim();
        if (before.isEmpty() || after.isEmpty()) {
            throw new NoriException("Please use this format: " + usage);
        }
        return new String[]{before, after};
    }

    private static void requireDescription(String description, String taskType) throws NoriException {
        if (description.isEmpty()) {
            throw new NoriException("The description of a " + taskType + " cannot be empty.");
        }
    }

    private static void requireNoArguments(String arguments, String command) throws NoriException {
        if (!arguments.isEmpty()) {
            throw new NoriException("The " + command + " command does not take any arguments.");
        }
    }

    private static void addTask(List<Task> tasks, Task task) {
        tasks.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        printTaskCount(tasks);
    }

    private static void printTaskCount(List<Task> tasks) {
        String taskWord = tasks.size() == 1 ? "task" : "tasks";
        System.out.println("Now you have " + tasks.size() + " " + taskWord + " in the list.");
    }
}
