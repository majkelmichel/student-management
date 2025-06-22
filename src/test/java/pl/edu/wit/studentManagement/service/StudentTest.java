package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for {@link Student} entity class.
 *
 * @author Martin Szum
 */
@DisplayName("Student Test Suite")
class StudentTest {

    @Test
    @DisplayName("Given valid student, when validate called, then do not throw")
    void givenValidStudent_whenValidateCalled_thenDoNotThrow() {
        // Arrange
        Student student = new Student("Adam", "Kowalski", "A12345");

        // Act & Assert
        assertDoesNotThrow(student::validate);
    }

    @Test
    @DisplayName("Given student with null first name, when validate called, then throws ValidationException")
    void givenStudentWithNullFirstName_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student(null, "Nowak", "A12345");

        // Act & Assert
        assertThrows(ValidationException.class, student::validate);
    }

    @Test
    @DisplayName("Given student with empty first name, when validate called, then throws ValidationException")
    void givenStudentWithEmptyFirstName_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("", "Nowak", "A12345");

        // Act & Assert
        assertThrows(ValidationException.class, student::validate);
    }

    @Test
    @DisplayName("Given student with null last name, when validate called, then throws ValidationException")
    void givenStudentWithNullLastName_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("Piotr", null, "A12345");

        // Act & Assert
        assertThrows(ValidationException.class, student::validate);
    }

    @Test
    @DisplayName("Given student with empty last name, when validate called, then throws ValidationException")
    void givenStudentWithEmptyLastName_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("Piotr", "", "A12345");

        // Act & Assert
        assertThrows(ValidationException.class, student::validate);
    }

    @Test
    @DisplayName("Given student with null album, when validate called, then throws ValidationException")
    void givenStudentWithNullAlbum_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("Marek", "Wiśniewski", null);

        // Act & Assert
        assertThrows(ValidationException.class, student::validate);
    }

    @Test
    @DisplayName("Given student with empty album, when validate called, then throws ValidationException")
    void givenStudentWithEmptyAlbum_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("Marek", "Wiśniewski", "");

        // Act & Assert
        assertThrows(ValidationException.class, student::validate);
    }

    @Test
    @DisplayName("Given student with invalid album format, when validate called, then throws ValidationException")
    void givenStudentWithInvalidAlbumFormat_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("Anna", "Wójcik", "123@456");

        // Act & Assert
        assertThrows(ValidationException.class, student::validate);
    }
}