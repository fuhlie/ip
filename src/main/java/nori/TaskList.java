package nori;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nori.task.Task;

/**
 * Owns the collection of tasks managed by Nori.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this(new ArrayList<>());
    }

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Finds tasks whose descriptions contain a keyword, ignoring letter case.
     *
     * @param keyword Keyword to find.
     * @return New task list containing the matching tasks.
     */
    public TaskList find(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matches.add(task);
            }
        }
        return new TaskList(matches);
    }
}
