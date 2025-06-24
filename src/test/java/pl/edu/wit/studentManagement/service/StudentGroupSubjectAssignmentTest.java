package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for {@link StudentGroupSubjectAssignment} entity class.
 *
 * @author Martin Szum
 */
@DisplayName("StudentGroupSubjectAssignment Test Suite")
class StudentGroupSubjectAssignmentTest {

    @Test
    @DisplayName("Given valid assignment, when validate called, then do not throw")
    void givenValidAssignment_whenValidateCalled_thenDoNotThrow() {
        // Arrange
        StudentGroupSubjectAssignment assignment = new StudentGroupSubjectAssignment(
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        // Act & Assert
        assertDoesNotThrow(assignment::validate);
    }

    @Test
    @DisplayName("Given assignment with null student group id, when validate called, then throws ValidationException")
    void givenAssignmentWithNullStudentGroupId_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        StudentGroupSubjectAssignment assignment = new StudentGroupSubjectAssignment(
                null,
                UUID.randomUUID()
        );

        // Act & Assert
        assertThrows(ValidationException.class, assignment::validate);
    }

    @Test
    @DisplayName("Given assignment with null subject id, when validate called, then throws ValidationException")
    void givenAssignmentWithNullSubjectId_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        StudentGroupSubjectAssignment assignment = new StudentGroupSubjectAssignment(
                UUID.randomUUID(),
                null
        );

        // Act & Assert
        assertThrows(ValidationException.class, assignment::validate);
    }
}