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
    private boolean shouldExit;

    /**
     * Creates a chatbot using the default save file and console UI.
     */
    public Nori() {
        this("data/nori.txt");
    }

    /**
     * Creates a chatbot that saves tasks at the given path.
     *
     * @param filePath Path of the task data file.
     */
    public Nori(String filePath) {
        storage = new Storage(filePath);
        ui = new Ui();
        try {
            tasks = storage.load();
        } catch (NoriException e) {
            tasks = new TaskList();
        }
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
        while (ui.hasNextCommand() && !shouldExit) {
            ui.show(getResponse(ui.readCommand()));
            if (!shouldExit) {
                ui.showLine();
            }
        }
        if (shouldExit) {
            ui.showLine();
        } else {
            ui.showGoodbye();
        }
    }

    /**
     * Processes one command and returns the text that should be shown to the user.
     *
     * @param input Command entered by the user.
     * @return Nori's response.
     */
    public String getResponse(String input) {
        try {
            String response = handle(input);
            storage.save(tasks);
            return response;
        } catch (NoriException e) {
            return e.getMessage();
        }
    }

    /**
     * Returns the greeting used by both the text and graphical interfaces.
     *
     * @return Nori's greeting.
     */
    public String getWelcomeMessage() {
        return "Hello! I'm Nori.\nWhat can I do for you?";
    }

    /**
     * Reports whether the user has entered the {@code bye} command.
     *
     * @return True if Nori should stop accepting commands.
     */
    public boolean shouldExit() {
        return shouldExit;
    }

    private String handle(String input) throws NoriException {
        ParsedCommand parsedCommand = Parser.parse(input);
        Command command = parsedCommand.getCommand();
        String arguments = parsedCommand.getArguments();

        switch (command) {
            case BYE:
                requireNoArguments(arguments, "bye");
                shouldExit = true;
                return "Bye. Hope to see you again soon!";
            case LIST:
                requireNoArguments(arguments, "list");
                return formatTasks(tasks, "Here are the tasks in your list:", "Your task list is empty.");
            case FIND:
                if (arguments.isEmpty()) {
                    throw new NoriException("The search keyword cannot be empty.");
                }
                return formatTasks(tasks.find(arguments), "Here are the matching tasks in your list:",
                        "I couldn't find any matching tasks.");
            case HELP:
                requireNoArguments(arguments, "help");
                return joinLines("Here are the commands you can use:",
                        "  todo DESCRIPTION",
                        "  deadline DESCRIPTION /by DATE",
                        "  event DESCRIPTION /from START /to END",
                        "  list",
                        "  find KEYWORD",
                        "  mark NUMBER",
                        "  unmark NUMBER",
                        "  delete NUMBER",
                        "  bye");
            case MARK:
                Task markedTask = tasks.get(parseTaskIndex(arguments));
                markedTask.mark();
                return joinLines("Nice! I've marked this task as done:", "  " + markedTask);
            case UNMARK:
                Task unmarkedTask = tasks.get(parseTaskIndex(arguments));
                unmarkedTask.unmark();
                return joinLines("OK, I've marked this task as not done yet:", "  " + unmarkedTask);
            case DELETE:
                Task deletedTask = tasks.remove(parseTaskIndex(arguments));
                return joinLines("Noted. I've removed this task:", "  " + deletedTask, formatTaskCount());
            case TODO:
                requireDescription(arguments, "todo");
                return addTask(new Todo(arguments));
            case DEADLINE:
                String[] deadlineParts = splitAround(arguments, " /by ", "deadline DESCRIPTION /by DATE");
                return addTask(new Deadline(deadlineParts[0], deadlineParts[1]));
            case EVENT:
                String[] fromParts = splitAround(arguments, " /from ", "event DESCRIPTION /from START /to END");
                String[] toParts = splitAround(fromParts[1], " /to ", "event DESCRIPTION /from START /to END");
                return addTask(new Event(fromParts[0], toParts[0], toParts[1]));
            default:
                throw new AssertionError("Unhandled command: " + command);
        }
    }

    private static String formatTasks(TaskList taskList, String heading, String emptyMessage) {
        if (taskList.isEmpty()) {
            return emptyMessage;
        }

        StringBuilder response = new StringBuilder(heading);
        for (int i = 0; i < taskList.size(); i++) {
            response.append(System.lineSeparator())
                    .append(i + 1)
                    .append(". ")
                    .append(taskList.get(i));
        }
        return response.toString();
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
        return new String[] { before, after };
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

    private String addTask(Task task) {
        tasks.add(task);
        return joinLines("Got it. I've added this task:", "  " + task, formatTaskCount());
    }

    private String formatTaskCount() {
        String taskWord = tasks.size() == 1 ? "task" : "tasks";
        return "Now you have " + tasks.size() + " " + taskWord + " in the list.";
    }

    /**
     * Joins any number of response lines using the platform line separator.
     *
     * @param lines Lines to join.
     * @return Lines combined into one response.
     */
    private static String joinLines(String... lines) {
        return String.join(System.lineSeparator(), lines);
    }
}
