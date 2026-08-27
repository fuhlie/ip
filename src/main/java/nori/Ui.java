package nori;

import java.util.Scanner;

/**
 * Handles text input and output for Nori.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    private final Scanner scanner = new Scanner(System.in);

    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void show(String message) {
        System.out.println(message);
    }

    public void showGoodbye() {
        show("Bye. Hope to see you again soon!");
        showLine();
    }

    public void showLine() {
        show(LINE);
    }

    public void showWelcome() {
        showLine();
        show("Hello! I'm Nori.");
        show("What can I do for you?");
        showLine();
    }
}
