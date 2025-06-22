
package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite for {@link StudentGroup} entity class.
 *
 * @author Martin Szum
 */
@DisplayName("StudentGroup Test Suite")
class StudentGroupTest {

    @Test
    @DisplayName("Given valid student group, when validate called, then do not throw")
    void givenValidStudentGroup_whenValidateCalled_thenDoNotThrow() {
        // Arrange
        StudentGroup group = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");

        // Act & Assert
        assertDoesNotThrow(group::validate);
    }

    @Test
    @DisplayName("Given student group with null code, when validate called, then throw validation exception")
    void givenStudentGroupWithNullCode_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        StudentGroup group = new StudentGroup(null, "Informatyka", "Grupa dzienna");

        // Act & Assert
        assertThrows(ValidationException.class, group::validate);
    }

    @Test
    @DisplayName("Given student group with empty code, when validate called, then throw validation exception")
    void givenStudentGroupWithEmptyCode_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        StudentGroup group = new StudentGroup("", "Informatyka", "Grupa dzienna");

        // Act & Assert
        assertThrows(ValidationException.class, group::validate);
    }

    @Test
    @DisplayName("Given student group with null specialization, when validate called, then throw validation exception")
    void givenStudentGroupWithNullSpecialization_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        StudentGroup group = new StudentGroup("INF2023", null, "Grupa dzienna");

        // Act & Assert
        assertThrows(ValidationException.class, group::validate);
    }

    @Test
    @DisplayName("Given student group with empty specialization, when validate called, then throw validation exception")
    void givenStudentGroupWithEmptySpecialization_whenValidateCalled_thenThrowValidationException() {
        // Arrange
        StudentGroup group = new StudentGroup("INF2023", "", "Grupa dzienna");

        // Act & Assert
        assertThrows(ValidationException.class, group::validate);
    }

    @Test
    @DisplayName("Given student group with null description, when validate called, then do not throw")
    void givenStudentGroupWithNullDescription_whenValidateCalled_thenDoNotThrow() {
        // Arrange
        StudentGroup group = new StudentGroup("INF2023", "Informatyka", null);

        // Act & Assert
        assertDoesNotThrow(group::validate);
    }
}