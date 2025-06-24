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
 * Test suite for {@link StudentGroupSubjectAssignmentDataStreamHandler} class.
 * 
 * @author Martin Szum
 */

class StudentGroupSubjectAssignmentDataStreamHandlerTest {

    private StudentGroupSubjectAssignmentDataStreamHandler handler;
    private Path tempFilePath;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempFilePath = tempDir.resolve("test-StudentGroupSubjectAssignments.dat");
        handler = new StudentGroupSubjectAssignmentDataStreamHandler(tempFilePath.toString());
    }

    @Test
    @DisplayName("Given new file, when readAll called, then return empty list")
    void givenNewFile_whenReadAllCalled_thenReturnEmptyList() throws IOException {
        // Act
        List<StudentGroupSubjectAssignment> result = handler.readAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given multiple StudentGroupSubjectAssignments written to file, when readAll called, then return list with all StudentGroupSubjectAssignments")
    void givenMultipleStudentGroupSubjectAssignmentsWrittenToFile_whenReadAllCalled_thenReturnListWithAllStudentGroupSubjectAssignments() throws IOException {
        // Arrange
        StudentGroupSubjectAssignment StudentGroupSubjectAssignment1 = new StudentGroupSubjectAssignment(UUID.randomUUID(), UUID.randomUUID());
        StudentGroupSubjectAssignment StudentGroupSubjectAssignment2 = new StudentGroupSubjectAssignment(UUID.randomUUID(), UUID.randomUUID());
        handler.write(StudentGroupSubjectAssignment1);
        handler.write(StudentGroupSubjectAssignment2);

        // Act
        List<StudentGroupSubjectAssignment> result = handler.readAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().map(StudentGroupSubjectAssignment::getId).anyMatch(id -> id.equals(StudentGroupSubjectAssignment1.getId())));
        assertTrue(result.stream().map(StudentGroupSubjectAssignment::getId).anyMatch(id -> id.equals(StudentGroupSubjectAssignment2.getId())));
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
    @DisplayName("When StudentGroupSubjectAssignment written and read, then all fields match")
    void whenStudentGroupSubjectAssignmentWrittenAndRead_thenAllFieldsMatch() throws IOException {
        // Arrange
        StudentGroupSubjectAssignment originalStudentGroupSubjectAssignment = new StudentGroupSubjectAssignment(UUID.randomUUID(), UUID.randomUUID());

        // Act
        handler.write(originalStudentGroupSubjectAssignment);
        List<StudentGroupSubjectAssignment> result = handler.readAll();

        // Assert
        assertEquals(1, result.size());
        StudentGroupSubjectAssignment readStudentGroupSubjectAssignment = result.get(0);
        assertEquals(originalStudentGroupSubjectAssignment.getId(), readStudentGroupSubjectAssignment.getId());
        assertEquals(originalStudentGroupSubjectAssignment.getStudentGroupId(), readStudentGroupSubjectAssignment.getStudentGroupId());
        assertEquals(originalStudentGroupSubjectAssignment.getSubjectId(), readStudentGroupSubjectAssignment.getSubjectId());
    }

    @Test
    @DisplayName("Given empty file, when write called, then file is created")
    void givenEmptyFile_whenWriteCalled_thenFileIsCreated() throws IOException {
        // Arrange
        StudentGroupSubjectAssignment StudentGroupSubjectAssignment = new StudentGroupSubjectAssignment(UUID.randomUUID(), UUID.randomUUID());

        // Act
        handler.write(StudentGroupSubjectAssignment);

        // Assert
        assertTrue(tempFilePath.toFile().exists());
        assertTrue(tempFilePath.toFile().length() > 0);
    }
}