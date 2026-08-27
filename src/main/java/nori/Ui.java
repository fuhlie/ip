package nori;

import java.util.Scanner;

/**
 * Handles text input and output for Nori.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a UI that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reports whether another command can be read.
     *
     * @return True if input remains available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command and removes surrounding whitespace.
     *
     * @return Next command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints a message followed by a line break.
     *
     * @param message Message to print.
     */
    public void show(String message) {
        System.out.println(message);
    }

    /**
     * Prints Nori's farewell and a separator.
     */
    public void showGoodbye() {
        show("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Prints a horizontal separator.
     */
    public void showLine() {
        show(LINE);
    }

    /**
     * Prints Nori's greeting.
     */
    public void showWelcome() {
        showLine();
        show("Hello! I'm Nori.");
        show("What can I do for you?");
        showLine();
    }
}
