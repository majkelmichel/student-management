package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for {@link GradeDao} class.
 *
 * @author Martin Szum
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GradeDao Test Suite")
class GradeDaoTest {

    @Mock
    private DataStreamHandler<Grade> dataStreamHandler;

    private GradeDao gradeDao;

    @BeforeEach
    void setUp() {
        gradeDao = new GradeDao(dataStreamHandler);
    }

    @Test
    @DisplayName("Given id of existing grade, when get called, then return correct grade")
    void givenExistingGrade_whenGetCalled_thenCorrectGrade() throws IOException {
        // Arrange
        Grade grade = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50);
        when(dataStreamHandler.readAll()).thenReturn(List.of(grade));

        // Act
        Optional<Grade> result = gradeDao.get(grade.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(grade, result.get());
    }

    @Test
    @DisplayName("Given id of non-existing grade, when get called, then return empty optional")
    void givenNonExistingGrade_whenGetCalled_thenEmptyOptional() throws IOException {
        // Arrange
        UUID gradeId = UUID.randomUUID();

        when(dataStreamHandler.readAll()).thenReturn(List.of());

        // Act
        Optional<Grade> result = gradeDao.get(gradeId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given list of existing grades, when getAll called, then return list of grades")
    void givenListOfGrades_whenGetAllCalled_thenReturnListOfGrades() throws IOException {
        // Arrange
        Grade g1 = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50);
        Grade g2 = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 60);

        when(dataStreamHandler.readAll()).thenReturn(List.of(g1, g2));

        // Act
        List<Grade> result = gradeDao.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(g1, result.get(0));
    }

    @Test
    @DisplayName("Given new valid grade entity, when save called, then save grade")
    void givenNewGrade_whenSaveCalled_thenSaveGrade() throws IOException, ValidationException {
        // Arrange
        Grade grade = spy(new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50));

        doNothing().when(dataStreamHandler).write(grade);

        // Act & Assert
        assertDoesNotThrow(() -> gradeDao.save(grade));
        verify(grade).validate();
    }

    @Test
    @DisplayName("Given existing valid grade, when update called, then update grad")
    void givenExistingValidGrade_whenUpdateCalled_thenUpdateGrade() throws IOException, ValidationException {
        // Arrange
        Grade grade = spy(new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50));

        doNothing().when(dataStreamHandler).update(grade);

        // Act & Assert
        assertDoesNotThrow(() -> gradeDao.update(grade));
        verify(grade).validate();
    }

    @Test
    @DisplayName("Given existing grade, when delete called, then delete grade")
    void givenExistingGrade_whenDeleteCalled_thenDeleteGrade() throws IOException {
        // Arrange
        Grade grade = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50);

        doNothing().when(dataStreamHandler).deleteById(grade.getId());

        // Act & Assert
        boolean result = assertDoesNotThrow(() -> gradeDao.delete(grade.getId()));
        assertTrue(result);
    }
}