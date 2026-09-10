package nori;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class NoriTest {
    @TempDir
    private Path tempDirectory;

    @Test
    void getResponse_addAndListCommands_returnsTaskDetails() {
        Nori nori = new Nori(tempDirectory.resolve("nori.txt").toString());

        assertTrue(nori.getResponse("todo read book").contains("[T][ ] read book"));
        assertTrue(nori.getResponse("list").contains("1. [T][ ] read book"));
    }

    @Test
    void getResponse_invalidCommand_returnsExplanation() {
        Nori nori = new Nori(tempDirectory.resolve("nori.txt").toString());

        assertTrue(nori.getResponse("dance").contains("Try help"));
    }

    @Test
    void getResponse_helpCommand_returnsCommandUsage() {
        Nori nori = new Nori(tempDirectory.resolve("nori.txt").toString());

        String response = nori.getResponse("help");

        assertTrue(response.contains("todo DESCRIPTION"));
        assertTrue(response.contains("deadline DESCRIPTION /by DATE"));
        assertTrue(response.contains("event DESCRIPTION /from START /to END"));
    }

    @Test
    void getResponse_byeCommand_setsExitState() {
        Nori nori = new Nori(tempDirectory.resolve("nori.txt").toString());

        assertEquals("Bye. Hope to see you again soon!", nori.getResponse("bye"));
        assertTrue(nori.shouldExit());
    }
}
