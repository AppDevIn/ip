package edith.exception;

/**
 * Exception thrown when there are issues with note operations on tasks.
 * This includes cases like empty note content or invalid task references.
 */
public class NoteException extends EdithException {
    
    /**
     * Creates a new NoteException with the specified error message.
     *
     * @param message the error message explaining what went wrong
     */
    public NoteException(String message) {
        super(message);
    }
}