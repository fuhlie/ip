import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that must be completed by a given time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDate by;

    public Deadline(String description, String by) throws NoriException {
        super(description);
        try {
            this.by = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            throw new NoriException("Please enter the deadline date as yyyy-MM-dd.");
        }
    }

    @Override
    protected String getTypeIcon() {
        return "D";
    }

    @Override
    public String toStorageString() {
        return "D | " + (isDone() ? "1" : "0") + " | " + description + " | " + by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
