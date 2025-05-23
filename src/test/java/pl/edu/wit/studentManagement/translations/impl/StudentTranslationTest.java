package pl.edu.wit.studentManagement.translations.impl;

import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.entities.fields.StudentField;

import static org.junit.jupiter.api.Assertions.*;
class StudentTranslationTest {
    @Test
    void name() {
        StudentTranslation studentTranslation = new StudentTranslation();
        studentTranslation.getTranslation(StudentField.FIRST_NAME);
    }
}