package nori;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import nori.task.Todo;

class TaskListTest {
    @Test
    void find_keywordWithDifferentCase_returnsMatchingTasks() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("Read Book"));
        tasks.add(new Todo("buy groceries"));

        TaskList matches = tasks.find("book");

        assertEquals(1, matches.size());
        assertEquals("[T][ ] Read Book", matches.get(0).toString());
    }
}
