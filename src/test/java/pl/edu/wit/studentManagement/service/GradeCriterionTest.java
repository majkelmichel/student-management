package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for {@link GradeCriterion} entity class.
 *
 * @author Martin Szum
 */
@DisplayName("GradeCriterion Test Suite")
class GradeCriterionTest {

    @Test
    @DisplayName("Given valid criterion, when validate called, then do not throw")
    void givenValidCriterion_whenValidateCalled_thenDoNotThrow() {
        // Arrange
        GradeCriterion criterion = new GradeCriterion("Test", (byte) 10, UUID.randomUUID());

        // Act & Assert
        assertDoesNotThrow(criterion::validate);
    }

    @Test
    @DisplayName("Given criterion with null name, when validate called, then throws ValidationException")
    void givenCriterionWithNullName_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        GradeCriterion criterion = new GradeCriterion(null, (byte) 10, UUID.randomUUID());

        // Act & Assert
        assertThrows(ValidationException.class, criterion::validate);
    }

    @Test
    @DisplayName("Given criterion with empty name, when validate called, then throws ValidationException")
    void givenCriterionWithEmptyName_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        GradeCriterion criterion = new GradeCriterion("", (byte) 10, UUID.randomUUID());

        // Act & Assert
        assertThrows(ValidationException.class, criterion::validate);
    }

    @Test
    @DisplayName("Given criterion with negative max points, when validate called, then throws ValidationException")
    void givenCriterionWithNegativeMaxPoints_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        GradeCriterion criterion = new GradeCriterion("Test", (byte) -1, UUID.randomUUID());

        // Act & Assert
        assertThrows(ValidationException.class, criterion::validate);
    }

    @Test
    @DisplayName("Given criterion with zero max points, when validate called, then throws ValidationException")
    void givenCriterionWithZeroMaxPoints_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        GradeCriterion criterion = new GradeCriterion("Test", (byte) 0, UUID.randomUUID());

        // Act & Assert
        assertThrows(ValidationException.class, criterion::validate);
    }
}