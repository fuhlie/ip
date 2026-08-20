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
        List<String> tasks = new ArrayList<>();
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
            } else {
                tasks.add(input);
                System.out.println("Added: " + input);
            }
            System.out.println(LINE);
        }

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
