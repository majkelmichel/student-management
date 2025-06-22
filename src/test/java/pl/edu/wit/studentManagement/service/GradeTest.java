package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for {@link Grade} entity class.
 *
 * @author Martin Szum
 */
@DisplayName("Grade Test Suite")
class GradeTest {

    @Test
    @DisplayName("Given valid grade, when validate called, then do not throw")
    void givenValidGrade_whenValidateCalled_thenDoNotThrow() {
        // Arrange
        Grade grade = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 4);

        // Act & Assert
        assertDoesNotThrow(grade::validate);
    }

    @Test
    @DisplayName("Given grade with null subject id, when validate called, then throws ValidationException")
    void givenGradeWithNullSubjectId_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Grade grade = new Grade(null, UUID.randomUUID(), UUID.randomUUID(), (byte) 4);

        // Act & Assert
        assertThrows(ValidationException.class, grade::validate);
    }

    @Test
    @DisplayName("Given grade with null grade criterion id, when validate called, then throws ValidationException")
    void givenGradeWithNullGradeCriterionId_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Grade grade = new Grade(UUID.randomUUID(), null, UUID.randomUUID(), (byte) 4);

        // Act & Assert
        assertThrows(ValidationException.class, grade::validate);
    }

    @Test
    @DisplayName("Given grade with null student id, when validate called, then throws ValidationException")
    void givenGradeWithNullStudentId_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Grade grade = new Grade(UUID.randomUUID(), UUID.randomUUID(), null, (byte) 4);

        // Act & Assert
        assertThrows(ValidationException.class, grade::validate);
    }

    @Test
    @DisplayName("Given grade with negative value, when validate called, then throws ValidationException")
    void givenGradeWithNegativeValue_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Grade grade = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) -1);

        // Act & Assert
        assertThrows(ValidationException.class, grade::validate);
    }

    @Test
    @DisplayName("Given grade with value greater than 100, when validate called, then throws ValidationException")
    void givenGradeWithValueGreaterThan100_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        Grade grade = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 101);

        // Act & Assert
        assertThrows(ValidationException.class, grade::validate);
    }
}