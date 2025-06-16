package pl.edu.wit.studentManagement.validation;


import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Custom exception for signaling validation errors with localization support.
 */
public class ValidationException extends Exception {
    private final String messageKey;
    private final Object[] messageArgs;

    public ValidationException(String messageKey, Object... messageArgs) {
        super();
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    /**
     * Returns a localized error message based on the default locale.
     */
    @Override
    public String getLocalizedMessage() {
        return getLocalizedMessage(Locale.getDefault());
    }

    /**
     * Returns a localized error message based on the given locale.
     */
    public String getLocalizedMessage(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", locale);
        String pattern = bundle.getString(messageKey);
        return MessageFormat.format(pattern, messageArgs);
    }
}
