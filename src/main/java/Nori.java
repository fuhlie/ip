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
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
            } else if (input.startsWith("mark ")) {
                Task task = tasks.get(Integer.parseInt(input.substring(5)) - 1);
                task.mark();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
            } else if (input.startsWith("unmark ")) {
                Task task = tasks.get(Integer.parseInt(input.substring(7)) - 1);
                task.unmark();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
            } else if (input.startsWith("todo ")) {
                addTask(tasks, new Todo(input.substring(5)));
            } else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split(" /by ", 2);
                addTask(tasks, new Deadline(parts[0], parts[1]));
            } else if (input.startsWith("event ")) {
                String[] parts = input.substring(6).split(" /from | /to ", 3);
                addTask(tasks, new Event(parts[0], parts[1], parts[2]));
            }
            System.out.println(LINE);
        }

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    private static void addTask(List<Task> tasks, Task task) {
        tasks.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }
}
