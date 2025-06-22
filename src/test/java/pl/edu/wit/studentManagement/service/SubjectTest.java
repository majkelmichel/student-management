package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for {@link Subject} entity class.
 *
 * @author Michał Zawadzki
 */
@DisplayName("Subject Test Suite")
class SubjectTest {

    @Test
    @DisplayName("Given valid subject, when validate called, then do not throw")
    void givenValidSubject_whenValidateCalled_thenDoNotThrow() {
        // Arrange
        Subject subject = new Subject("Matematyka dystkretna");

        // Act & Assert
        assertDoesNotThrow(subject::validate);
    }

    @Test
    @DisplayName("Given subject with null name, when validate called, then throws ValidationException")
    void givenSubjectWithNullName_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Subject subject = new Subject(null);

        // Act & Assert
        assertThrows(ValidationException.class, subject::validate);
    }

    @Test
    @DisplayName("Given subject with empty name, when validate called, then throws ValidationException")
    void givenSubjectWithEmptyName_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Subject subject = new Subject("");

        // Act & Assert
        assertThrows(ValidationException.class, subject::validate);
    }
}