package pl.edu.wit.studentManagement.translations.impl;

import pl.edu.wit.studentManagement.entities.fields.StudentField;
import pl.edu.wit.studentManagement.translations.interfaces.Translation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Translation implementation for {@link StudentField} values.
 *
 * <p>This class provides localized (Polish) string representations for
 * fields related to a student, such as first name, last name, and album number.
 * It returns a default message for unknown or null fields.</p>
 *
 * @author Michał Pokrzywka
 */

public class StudentTranslation implements Translation<StudentField> {
    private final ResourceBundle resourceBundle;

    /**
     * Constructor initializes the ResourceBundle for the specified locale.
     *
     * @param locale the locale for which translations should be loaded
     */
    public StudentTranslation(Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Returns the localized translation for a given StudentField.
     *
     * @param studentField the field to translate
     * @return the localized string corresponding to the field, or a default value if null
     */
    @Override
    public String getTranslation(StudentField studentField) {
        if (studentField == null) {
            return resourceBundle.getString("student.unknown");
        }
        String key;

        switch (studentField) {
            case FIRST_NAME:
                key = "student.first_name";
                break;
            case LAST_NAME:
                key = "student.last_name";
                break;
            case ALBUM:
                key = "student.album";
                break;
            default:
                key = "student.unknown";
                break;
        }
        return resourceBundle.getString(key);
    }
}
