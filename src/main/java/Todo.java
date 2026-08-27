/**
 * Represents a task without an associated date or time.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    protected String getTypeIcon() {
        return "T";
    }

    @Override
    public String toStorageString() {
        return "T | " + (isDone() ? "1" : "0") + " | " + description;
    }
}
