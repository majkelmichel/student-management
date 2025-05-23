package pl.edu.wit.studentManagement.validation;

import pl.edu.wit.studentManagement.entities.fields.EntityField;
import pl.edu.wit.studentManagement.entities.fields.StudentField;

public class ValidationError {
    private final EntityField fieldName;
    private final ValidationErrorType error;

    public ValidationError(EntityField fieldName, ValidationErrorType error) {
        this.fieldName = fieldName;
        this.error = error;
    }

    public EntityField getFieldName() {
        return fieldName;
    }

    public ValidationErrorType getError() {
        return error;
    }
}
