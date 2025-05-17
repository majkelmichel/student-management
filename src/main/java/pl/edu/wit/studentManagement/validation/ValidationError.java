package pl.edu.wit.studentManagement.validation;

import pl.edu.wit.studentManagement.entities.fields.StudentField;

public class ValidationError {
    private final StudentField fieldName;
    private final ValidationErrorType error;

    public ValidationError(StudentField fieldName, ValidationErrorType error) {
        this.fieldName = fieldName;
        this.error = error;
    }

    public StudentField getFieldName() {
        return fieldName;
    }

    public ValidationErrorType getError() {
        return error;
    }
}
