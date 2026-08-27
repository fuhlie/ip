package nori;

import java.util.Locale;

/**
 * Identifies the commands understood by Nori.
 */
public enum Command {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT;

    /**
     * Converts user input to a command, reporting unknown command words clearly.
     */
    public static Command from(String word) throws NoriException {
        try {
            return valueOf(word.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new NoriException("I don't recognise that command. "
                    + "Try todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
    }
}
