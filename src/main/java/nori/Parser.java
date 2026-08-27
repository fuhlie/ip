package nori;

/**
 * Converts raw user input into commands and arguments.
 */
public final class Parser {
    private Parser() {
    }

    public static ParsedCommand parse(String input) throws NoriException {
        String[] commandParts = input.trim().split("\\s+", 2);
        Command command = Command.from(commandParts[0]);
        String arguments = commandParts.length == 2 ? commandParts[1].trim() : "";
        return new ParsedCommand(command, arguments);
    }
}
