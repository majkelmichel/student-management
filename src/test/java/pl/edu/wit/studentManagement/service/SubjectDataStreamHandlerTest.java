package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for {@link SubjectDataStreamHandler} class.
 *
 * @author Michał Zawadzki
 */
@DisplayName("SubjectDataStreamHandler Test Suite")
class SubjectDataStreamHandlerTest {

    @TempDir
    Path tempDir;

    SubjectDataStreamHandler handler;
    File dataFile;

    @BeforeEach
    void setUp() {
        dataFile = tempDir.resolve("subjects.dat").toFile();
        handler = new SubjectDataStreamHandler(dataFile.getAbsolutePath());
    }

    @Test
    @DisplayName("Given empty file, when readAll is called, then return empty list")
    void givenEmptyFile_whenReadAllIsCalled_thenReturnEmptyList() throws IOException {
        // Arrange done in setUp

        // Act
        List<Subject> result = handler.readAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given valid subject, when write is called, then subject is persisted")
    void givenValidSubject_whenWriteIsCalled_thenSubjectIsPersisted() throws IOException {
        // Arrange
        Subject subject = new Subject("Matematyka Dyskretna");

        // Act
        handler.write(subject);
        List<Subject> subjects = handler.readAll();

        // Assert
        assertEquals(1, subjects.size());
        assertEquals("Matematyka Dyskretna", subjects.get(0).getName());
    }

    @Test
    @DisplayName("Given persisted subject, when update is called with new name, then subject is updated")
    void givenPersistedSubject_whenUpdateIsCalledWithNewName_thenSubjectIsUpdated() throws IOException {
        // Arrange
        Subject subject = new Subject("Programowanie w C++");
        handler.write(subject);

        // Act
        subject.setName("Programowanie w Javie");
        handler.update(subject);
        List<Subject> subjects = handler.readAll();

        // Assert
        assertEquals(1, subjects.size());
        assertEquals("Programowanie w Javie", subjects.get(0).getName());
    }

    @Test
    @DisplayName("Given no matching subject, when update is called, then IOException is thrown")
    void givenNoMatchingSubject_whenUpdateIsCalledWithNewName_thenIOExceptionIsThrown() {
        // Arrange
        Subject subject = new Subject("Sieci Komputerowe");

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> handler.update(subject));
        assertTrue(exception.getMessage().contains("Subject not found"));
    }

    @Test
    @DisplayName("Given persisted subject, when deleteById is called, then subject is removed")
    void givenPersistedSubject_whenDeleteByIdIsCalled_thenSubjectIsRemoved() throws IOException {
        // Arrange
        Subject subject = new Subject("Algorytmy i Struktury Danych");
        handler.write(subject);

        // Act
        handler.deleteById(subject.getId());
        List<Subject> subjects = handler.readAll();

        // Assert
        assertTrue(subjects.isEmpty());
    }

    @Test
    @DisplayName("Given no matching ID, when deleteById is called, then IOException is thrown")
    void givenNoMatchingSubject_whenDeleteByIdIsCalled_thenIOExceptionIsThrown() {
        // Arrange
        UUID randomId = UUID.randomUUID();

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> handler.deleteById(randomId));
        assertTrue(exception.getMessage().contains("Subject not found"));
    }
}
