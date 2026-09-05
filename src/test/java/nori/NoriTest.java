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

        assertTrue(nori.getResponse("dance").contains("don't recognise"));
    }

    @Test
    void getResponse_byeCommand_setsExitState() {
        Nori nori = new Nori(tempDirectory.resolve("nori.txt").toString());

        assertEquals("Bye. Hope to see you again soon!", nori.getResponse("bye"));
        assertTrue(nori.shouldExit());
    }
}
