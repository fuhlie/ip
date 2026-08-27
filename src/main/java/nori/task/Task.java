package nori.task;

/**
 * Represents a task and whether it has been completed.
 */
public abstract class Task {
    protected final String description;
    private boolean isDone;

    protected Task(String description) {
        this.description = description;
    }

    public void mark() {
        isDone = true;
    }

    public void unmark() {
        isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    public abstract String toStorageString();

    private String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    protected abstract String getTypeIcon();

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
