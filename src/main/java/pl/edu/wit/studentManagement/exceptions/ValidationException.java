package pl.edu.wit.studentManagement.exceptions;

/**
 * Exception thrown to indicate that a validation error has occurred.
 * <p>
 * This exception supports message localization by storing a message key
 * and optional arguments, which can be used to generate localized error messages
 * at a higher application layer (e.g., in a UI or REST API response).
 *
 * <p>
 * It is intended for use in service or domain layers to signal that
 * user-provided data or state violates business rules or input constraints.
 *
 * @see Exception
 */
public class ValidationException extends Exception {
    private final String messageKey;
    private final Object[] messageArgs;

    /**
     * Constructs a new ValidationException with the given message key and optional arguments.
     *
     * @param messageKey  a key referencing a localized error message
     * @param messageArgs optional arguments to be inserted into the localized message
     */
    public ValidationException(String messageKey, Object... messageArgs) {
        super();
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    /**
     * Returns the message key for localization.
     *
     * @return the message key associated with this validation error
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Returns the arguments used in the localized message.
     *
     * @return an array of arguments for message formatting
     */
    public Object[] getMessageArgs() {
        return messageArgs;
    }
}
