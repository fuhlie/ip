package nori;

/**
 * Converts raw user input into commands and arguments.
 */
public class Parser {
    private Parser() {
    }

    /**
     * Separates the first word of user input from its arguments.
     *
     * @param input Raw user input.
     * @return Parsed command and arguments.
     * @throws NoriException If the command word is not recognized.
     */
    public static ParsedCommand parse(String input) throws NoriException {
        String[] commandParts = input.trim().split("\\s+", 2);
        Command command = Command.from(commandParts[0]);
        String arguments = commandParts.length == 2 ? commandParts[1].trim() : "";
        return new ParsedCommand(command, arguments);
    }
}
