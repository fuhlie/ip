package nori;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import nori.task.Deadline;
import nori.task.Todo;

class StorageTest {
    @TempDir
    private Path tempDirectory;

    @Test
    void load_missingFile_returnsEmptyTaskList() throws NoriException {
        Storage storage = new Storage(tempDirectory.resolve("missing.txt").toString());

        assertTrue(storage.load().isEmpty());
    }

    @Test
    void saveAndLoad_tasks_restoresTaskData() throws NoriException {
        Storage storage = new Storage(tempDirectory.resolve("nori.txt").toString());
        TaskList tasks = new TaskList();
        Todo todo = new Todo("read book");
        todo.mark();
        tasks.add(todo);
        tasks.add(new Deadline("submit iP", "2026-08-28"));

        storage.save(tasks);
        TaskList loadedTasks = storage.load();

        assertEquals(2, loadedTasks.size());
        assertEquals("[T][X] read book", loadedTasks.get(0).toString());
        assertEquals("[D][ ] submit iP (by: Aug 28 2026)", loadedTasks.get(1).toString());
    }
}
