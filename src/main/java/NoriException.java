/**
 * Represents an invalid command or argument supplied to Nori.
 */
public class NoriException extends Exception {
    private static final long serialVersionUID = 1L;

    public NoriException(String message) {
        super(message);
    }
}
