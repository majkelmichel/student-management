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
 * Test suite for {@link GradeDataStreamHandler} class.
 *
 * @author Martin Szum
 */
class GradeDataStreamHandlerTest {

    private GradeDataStreamHandler handler;
    private Path tempFilePath;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempFilePath = tempDir.resolve("test-grades.dat");
        handler = new GradeDataStreamHandler(tempFilePath.toString());
    }

    @Test
    @DisplayName("Given new file, when readAll called, then return empty list")
    void givenNewFile_whenReadAllCalled_thenReturnEmptyList() throws IOException {
        // Act
        List<Grade> result = handler.readAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given multiple grades written to file, when readAll called, then return list with all grades")
    void givenMultipleGradesWrittenToFile_whenReadAllCalled_thenReturnListWithAllGrades() throws IOException {
        // Arrange
        Grade grade1 = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50);
        Grade grade2 = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 100);
        UUID gradeId1 = grade1.getId();
        UUID gradeId2 = grade2.getId();
        handler.write(grade1);
        handler.write(grade2);

        // Act
        List<Grade> result = handler.readAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().map(Grade::getGrade).anyMatch(grade -> grade == 50));
        assertTrue(result.stream().map(Grade::getGrade).anyMatch(grade -> grade == 100));
        assertTrue(result.stream().map(Grade::getId).anyMatch(gradeId -> gradeId.equals(gradeId1)));
        assertTrue(result.stream().map(Grade::getId).anyMatch(gradeId -> gradeId.equals(gradeId2)));
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
    @DisplayName("When grade written and read, then all fields match")
    void whenGradeWrittenAndRead_thenAllFieldsMatch() throws IOException {
        // Arrange
        Grade originalGrade = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50);

        // Act
        handler.write(originalGrade);
        List<Grade> result = handler.readAll();

        // Assert
        assertEquals(1, result.size());
        Grade readGrade = result.get(0);
        assertEquals(originalGrade.getId(), readGrade.getId());
        assertEquals(originalGrade.getGradeCriterionId(), readGrade.getGradeCriterionId());
        assertEquals(originalGrade.getStudentId(), readGrade.getStudentId());
        assertEquals(originalGrade.getGrade(), readGrade.getGrade());
    }

    @Test
    @DisplayName("Given empty file, when write called, then file is created")
    void givenEmptyFile_whenWriteCalled_thenFileIsCreated() throws IOException {
        // Arrange
        Grade grade = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50);

        // Act
        handler.write(grade);

        // Assert
        assertTrue(tempFilePath.toFile().exists());
        assertTrue(tempFilePath.toFile().length() > 0);
    }
}