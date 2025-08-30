package duke.exception;

/**
 * Exception thrown when there are issues with the find command input.
 */
public class FindException extends DukeException {
    /**
     * Creates a FindException with the specified error message.
     *
     * @param message the error message describing what went wrong
     */
    public FindException(String message) {
        super(message);
    }
}