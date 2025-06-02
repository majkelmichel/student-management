package pl.edu.wit.studentManagement.translations.impl;

import pl.edu.wit.studentManagement.entities.fields.StudentField;
import pl.edu.wit.studentManagement.translations.interfaces.Translation;

public class StudentTranslation implements Translation<StudentField> {

    @Override
    public String getTranslation(StudentField studentField) {
        switch (studentField) {
            // TODO: implement translations
            default: {
                return studentField.getFieldName();
            }
        }
    }
}
