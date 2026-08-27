/**
 * Contains a parsed command and its remaining arguments.
 */
public class ParsedCommand {
    private final String arguments;
    private final Command command;

    public ParsedCommand(Command command, String arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public Command getCommand() {
        return command;
    }

    public String getArguments() {
        return arguments;
    }
}
