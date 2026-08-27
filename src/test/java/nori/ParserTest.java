package nori;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ParserTest {
    @Test
    void parse_todoCommand_returnsCommandAndArguments() throws NoriException {
        ParsedCommand parsedCommand = Parser.parse("todo read book");

        assertEquals(Command.TODO, parsedCommand.getCommand());
        assertEquals("read book", parsedCommand.getArguments());
    }

    @Test
    void parse_unknownCommand_throwsNoriException() {
        assertThrows(NoriException.class, () -> Parser.parse("unknown command"));
    }
}
