package pl.edu.wit.studentManagement.entities.fields;

import pl.edu.wit.studentManagement.entities.Student;

/**
 * Utility enum containing names of fields of {@link Student} class
 * @author Michał Zawadzki
 */
public enum StudentField implements EntityField {
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    ALBUM("album");

    private final String fieldName;

    StudentField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
