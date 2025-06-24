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
 * Test suite for {@link GradeCriterionDataStreamHandler} class.
 *
 * @author Martin Szum
 */

@DisplayName( "GradeCriterionDataStreamHandler Test Suite")
class GradeCriterionDataStreamHandlerTest {

    private GradeCriterionDataStreamHandler handler;
    private Path tempFilePath;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempFilePath = tempDir.resolve("test-grade-criteria.dat");
        handler = new GradeCriterionDataStreamHandler(tempFilePath.toString());
    }

    @Test
    @DisplayName("Given new file, when readAll called, then return empty list")
    void givenNewFile_whenReadAllCalled_thenReturnEmptyList() throws IOException {
        // Act
        List<GradeCriterion> result = handler.readAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given multiple grade criteria written to file, when readAll called, then return list with all grade criteria")
    void givenMultipleGradeCriteriaWrittenToFile_whenReadAllCalled_thenReturnListWithAllGradeCriteria() throws IOException {
        // Arrange
        GradeCriterion gradeCriterion1 = new GradeCriterion(UUID.randomUUID(), "Egzamin", (byte) 50, UUID.randomUUID());
        GradeCriterion gradeCriterion2 = new GradeCriterion(UUID.randomUUID(), "Praca domowa", (byte) 100, UUID.randomUUID());
        handler.write(gradeCriterion1);
        handler.write(gradeCriterion2);

        // Act
        List<GradeCriterion> result = handler.readAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().map(GradeCriterion::getName).anyMatch(name -> name.equals("Egzamin")));
        assertTrue(result.stream().map(GradeCriterion::getName).anyMatch(name -> name.equals("Praca domowa")));
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
    @DisplayName("When grade criterion written and read, then all fields match")
    void whenGradeCriterionWrittenAndRead_thenALlFieldsMatch() throws IOException {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Egzamin";
        byte maxPoints = 50;
        UUID subjectId = UUID.randomUUID();
        GradeCriterion originalGradeCriterion = new GradeCriterion(id, name, maxPoints, subjectId);

        // Act
        handler.write(originalGradeCriterion);
        List<GradeCriterion> result = handler.readAll();

        // Assert
        assertEquals(1, result.size());
        GradeCriterion readGradeCriterion = result.get(0);
        assertEquals(id, readGradeCriterion.getId());
        assertEquals(name, readGradeCriterion.getName());
        assertEquals(subjectId, readGradeCriterion.getSubjectId());
        assertEquals(maxPoints, readGradeCriterion.getMaxPoints());
    }

    @Test
    @DisplayName("Given empty file, when write called, then file is created")
    void givenEmptyFile_whenWriteCalled_thenFileIsCreated() throws IOException {
        // Arrange
        GradeCriterion gradeCriterion = new GradeCriterion(UUID.randomUUID(), "Egzamin", (byte) 50, UUID.randomUUID());

        // Act
        handler.write(gradeCriterion);

        // Assert
        assertTrue(tempFilePath.toFile().exists());
        assertTrue(tempFilePath.toFile().length() > 0);
    }
}