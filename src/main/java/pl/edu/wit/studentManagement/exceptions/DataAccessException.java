package pl.edu.wit.studentManagement.exceptions;

/**
 * Exception indicating a failure during data access operations.
 * <p>
 * This exception wraps lower-level I/O or persistence-related errors
 * (e.g. file system failure, database access issues) and rethrows them
 * as unchecked exceptions to avoid polluting the service layer with technical details.
 *
 * <p>
 * Typical usage involves catching a low-level {@link java.io.IOException}
 * or similar exception and rethrowing it as a {@code DataAccessException}.
 *
 * @see RuntimeException
 */
public class DataAccessException extends RuntimeException {
    private final String messageKey;

    /**
     * Constructs a new DataAccessException with the specified detail message and cause.
     *
     * @param messageKey a key referencing a localized error message
     * @param cause   the underlying cause of the exception
     */
    public DataAccessException(String messageKey, Throwable cause) {
        super(messageKey, cause);
        this.messageKey = messageKey;
    }

    /**
     * Returns the message key for localization.
     *
     * @return the message key associated with this validation error
     */
    public String getMessageKey() {
        return messageKey;
    }
}
