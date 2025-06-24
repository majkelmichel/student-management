package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for {@link StudentGroupDataStreamHandler} class.
 * 
 * @author Martin Szum
 */

class StudentGroupDataStreamHandlerTest {
    
    private StudentGroupDataStreamHandler handler;
    private Path tempFilePath;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempFilePath = tempDir.resolve("test-StudentGroups.dat");
        handler = new StudentGroupDataStreamHandler(tempFilePath.toString());
    }

    @Test
    @DisplayName("Given new file, when readAll called, then return empty list")
    void givenNewFile_whenReadAllCalled_thenReturnEmptyList() throws IOException {
        // Act
        List<StudentGroup> result = handler.readAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given multiple StudentGroups written to file, when readAll called, then return list with all StudentGroups")
    void givenMultipleStudentGroupsWrittenToFile_whenReadAllCalled_thenReturnListWithAllStudentGroups() throws IOException {
        // Arrange
        StudentGroup StudentGroup1 = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        StudentGroup StudentGroup2 = new StudentGroup("INF2024", "Informatyka", "Grupa zaoczna");
        handler.write(StudentGroup1);
        handler.write(StudentGroup2);

        // Act
        List<StudentGroup> result = handler.readAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().map(StudentGroup::getCode).anyMatch(name -> name.equals("INF2023")));
        assertTrue(result.stream().map(StudentGroup::getCode).anyMatch(name -> name.equals("INF2024")));
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
    @DisplayName("When StudentGroup written and read, then all fields match")
    void whenStudentGroupWrittenAndRead_thenAllFieldsMatch() throws IOException {
        // Arrange
        StudentGroup originalStudentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");

        // Act
        handler.write(originalStudentGroup);
        List<StudentGroup> result = handler.readAll();

        // Assert
        assertEquals(1, result.size());
        StudentGroup readStudentGroup = result.get(0);
        assertEquals(originalStudentGroup.getId(), readStudentGroup.getId());
        assertEquals(originalStudentGroup.getCode(), readStudentGroup.getCode());
        assertEquals(originalStudentGroup.getSpecialization(), readStudentGroup.getSpecialization());
        assertEquals(originalStudentGroup.getDescription(), readStudentGroup.getDescription());
    }

    @Test
    @DisplayName("Given empty file, when write called, then file is created")
    void givenEmptyFile_whenWriteCalled_thenFileIsCreated() throws IOException {
        // Arrange
        StudentGroup StudentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");

        // Act
        handler.write(StudentGroup);

        // Assert
        assertTrue(tempFilePath.toFile().exists());
        assertTrue(tempFilePath.toFile().length() > 0);
    }
}