/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    protected final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
    }

    public void mark() {
        isDone = true;
    }

    public void unmark() {
        isDone = false;
    }

    private String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
