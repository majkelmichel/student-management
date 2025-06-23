package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
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

    private SubjectDataStreamHandler handler;
    private Path tempFilePath;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempFilePath = tempDir.resolve("test-subjects.dat");
        handler = new SubjectDataStreamHandler(tempFilePath.toString());
    }

    @Test
    @DisplayName("Given new file, when readAll called, then return empty list")
    void givenNewFile_whenReadAllCalled_thenReturnEmptyList() throws IOException {
        // Act
        List<Subject> result = handler.readAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given subject written to file, when readAll called, then return list with subject")
    void givenSubjectWrittenToFile_whenReadAllCalled_thenReturnListWithSubject() throws IOException {
        // Arrange
        UUID id = UUID.randomUUID();
        Subject subject = new Subject(id, "Matematyka Dyskretna");
        handler.write(subject);

        // Act
        List<Subject> result = handler.readAll();

        // Assert
        assertEquals(1, result.size());
        Subject readSubject = result.get(0);
        assertEquals(id, readSubject.getId());
        assertEquals("Matematyka Dyskretna", readSubject.getName());
    }

    @Test
    @DisplayName("Given multiple subjects written to file, when readAll called, then return list with all subjects")
    void givenMultipleSubjectsWrittenToFile_whenReadAllCalled_thenReturnListWithAllSubjects() throws IOException {
        // Arrange
        Subject subject1 = new Subject(UUID.randomUUID(), "Fizyka 1");
        Subject subject2 = new Subject(UUID.randomUUID(), "Język Java");
        handler.write(subject1);
        handler.write(subject2);

        // Act
        List<Subject> result = handler.readAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().map(Subject::getName).anyMatch(name -> name.equals("Fizyka 1")));
        assertTrue(result.stream().map(Subject::getName).anyMatch(name -> name.equals("Język Java")));
    }

    @Test
    @DisplayName("Given corrupt data in file, when readObject called, then return null")
    void givenCorruptDataInFile_whenReadObjectCalled_thenReturnNull() throws IOException {
        // Arrange
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFilePath.toFile()))) {
            out.writeBytes("losowe złe dane");
        }

        // Act & Assert
        try (DataInputStream in = new DataInputStream(new FileInputStream(tempFilePath.toFile()))) {
            assertNull(handler.readObject(in));
        }
    }

    @Test
    @DisplayName("When subject written and read, then all fields match")
    void whenSubjectWrittenAndRead_thenAllFieldsMatch() throws IOException {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Inżynieria Oprogramowania";
        Subject originalSubject = new Subject(id, name);

        // Act
        handler.write(originalSubject);
        List<Subject> result = handler.readAll();

        // Assert
        assertEquals(1, result.size());
        Subject readSubject = result.get(0);
        assertEquals(id, readSubject.getId());
        assertEquals(name, readSubject.getName());
    }

    @Test
    @DisplayName("Given empty file, when write called, then file is created")
    void givenEmptyFile_whenWriteCalled_thenFileIsCreated() throws IOException {
        // Arrange
        Subject subject = new Subject(UUID.randomUUID(), "Język C#");

        // Act
        handler.write(subject);

        // Assert
        assertTrue(tempFilePath.toFile().exists());
        assertTrue(tempFilePath.toFile().length() > 0);
    }
}