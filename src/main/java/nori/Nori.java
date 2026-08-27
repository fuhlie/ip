package nori;

import nori.task.Deadline;
import nori.task.Event;
import nori.task.Task;
import nori.task.Todo;

/**
 * Coordinates the components of the Nori task-tracking chatbot.
 */
public class Nori {
    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Creates a chatbot using the default save file and console UI.
     */
    public Nori() {
        storage = new Storage("data/nori.txt");
        ui = new Ui();
    }

    /**
     * Starts Nori as a command-line application.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Nori().run();
    }

    /**
     * Loads saved tasks and processes commands until the user exits.
     */
    public void run() {
        ui.showWelcome();
        try {
            tasks = storage.load();
        } catch (NoriException e) {
            ui.show(e.getMessage());
            tasks = new TaskList();
        }

        boolean shouldExit = false;
        while (ui.hasNextCommand() && !shouldExit) {
            try {
                shouldExit = handle(ui.readCommand());
                storage.save(tasks);
            } catch (NoriException e) {
                ui.show(e.getMessage());
            }
            if (!shouldExit) {
                ui.showLine();
            }
        }
        ui.showGoodbye();
    }

    private boolean handle(String input) throws NoriException {
        ParsedCommand parsedCommand = Parser.parse(input);
        Command command = parsedCommand.getCommand();
        String arguments = parsedCommand.getArguments();

        switch (command) {
        case BYE:
            requireNoArguments(arguments, "bye");
            return true;
        case LIST:
            requireNoArguments(arguments, "list");
            showTasks();
            break;
        case MARK:
            Task markedTask = tasks.get(parseTaskIndex(arguments));
            markedTask.mark();
            ui.show("Nice! I've marked this task as done:");
            ui.show("  " + markedTask);
            break;
        case UNMARK:
            Task unmarkedTask = tasks.get(parseTaskIndex(arguments));
            unmarkedTask.unmark();
            ui.show("OK, I've marked this task as not done yet:");
            ui.show("  " + unmarkedTask);
            break;
        case DELETE:
            Task deletedTask = tasks.remove(parseTaskIndex(arguments));
            ui.show("Noted. I've removed this task:");
            ui.show("  " + deletedTask);
            printTaskCount();
            break;
        case TODO:
            requireDescription(arguments, "todo");
            addTask(new Todo(arguments));
            break;
        case DEADLINE:
            String[] deadlineParts = splitAround(arguments, " /by ", "deadline DESCRIPTION /by DATE");
            addTask(new Deadline(deadlineParts[0], deadlineParts[1]));
            break;
        case EVENT:
            String[] fromParts = splitAround(arguments, " /from ", "event DESCRIPTION /from START /to END");
            String[] toParts = splitAround(fromParts[1], " /to ", "event DESCRIPTION /from START /to END");
            addTask(new Event(fromParts[0], toParts[0], toParts[1]));
            break;
        }
        return false;
    }

    private void showTasks() {
        if (tasks.isEmpty()) {
            ui.show("Your task list is empty.");
            return;
        }
        ui.show("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.show((i + 1) + ". " + tasks.get(i));
        }
    }

    private int parseTaskIndex(String argument) throws NoriException {
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

    private void addTask(Task task) {
        tasks.add(task);
        ui.show("Got it. I've added this task:");
        ui.show("  " + task);
        printTaskCount();
    }

    private void printTaskCount() {
        String taskWord = tasks.size() == 1 ? "task" : "tasks";
        ui.show("Now you have " + tasks.size() + " " + taskWord + " in the list.");
    }
}
