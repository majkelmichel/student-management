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
 * Test suite for {@link SubjectDao} class.
 *
 * @author Michał Zawadzki
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SubjectDao Test Suite")
class SubjectDaoTest {

    @Mock
    private DataStreamHandler<Subject> dataStreamHandler;

    private SubjectDao subjectDao;

    @BeforeEach
    void setUp() {
        subjectDao = new SubjectDao(dataStreamHandler);
    }

    @Test
    @DisplayName("Given id of existing subject, when get called, then return correct subject")
    void givenExistingSubject_whenGetCalled_thenCorrectSubject() throws IOException {
        // Arrange
        Subject subject = new Subject("Grafy i sieci");

        when(dataStreamHandler.readAll()).thenReturn(List.of(subject));

        // Act
        Optional<Subject> result = subjectDao.get(subject.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(subject, result.get());
    }

    @Test
    @DisplayName("Given id of non-existing subject, when get called, then return empty optional")
    void givenNonExistingSubject_whenGetCalled_thenEmptyOptional() throws IOException {
        // Arrange
        UUID subjectId = UUID.randomUUID();

        when(dataStreamHandler.readAll()).thenReturn(List.of());

        // Act
        Optional<Subject> result = subjectDao.get(subjectId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given list of existing subjects, when getAll called, then return list of subjects")
    void givenListOfSubjects_whenGetAllCalled_thenReturnListOfSubjects() throws IOException {
        // Arrange
        Subject s1 = new Subject("Grafy i sieci");
        Subject s2 = new Subject("Kombinatoryka");

        when(dataStreamHandler.readAll()).thenReturn(List.of(s1, s2));

        // Act
        List<Subject> result = subjectDao.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(s1, result.get(0));
    }

    @Test
    @DisplayName("Given new valid subject entity, when save called, then save subject")
    void givenNewSubject_whenSaveCalled_thenSaveSubject() throws IOException, ValidationException {
        // Arrange
        Subject subject = spy(new Subject("Grafy i sieci"));

        doNothing().when(dataStreamHandler).write(subject);

        // Act & Assert
        assertDoesNotThrow(() -> subjectDao.save(subject));
        verify(subject).validate();
    }

    @Test
    @DisplayName("Given existing valid existing subject, when update called, then update subject")
    void givenExistingValidSubject_whenUpdateCalled_thenUpdateSubject() throws IOException, ValidationException {
        // Arrange
        Subject subject = spy(new Subject("Analiza Matematyczna"));

        doNothing().when(dataStreamHandler).update(subject);

        // Act & Assert
        assertDoesNotThrow(() -> subjectDao.update(subject));
        verify(subject).validate();
    }

    @Test
    @DisplayName("Given existing subject, when delete called, then delete subject")
    void givenExistingSubject_whenDeleteCalled_thenDeleteSubject() throws IOException {
        // Arrange
        Subject subject = new Subject("Grafy i sieci");

        doNothing().when(dataStreamHandler).deleteById(subject.getId());

        // Act & Assert
        boolean result = assertDoesNotThrow(() -> subjectDao.delete(subject.getId()));
        assertTrue(result);
    }
}