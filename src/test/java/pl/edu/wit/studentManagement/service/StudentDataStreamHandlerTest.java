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
 * Test suite for {@link StudentDataStreamHandler} class.
 * 
 * @author Martin Szum
 */

class StudentDataStreamHandlerTest {

    private StudentDataStreamHandler handler;
    private Path tempFilePath;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempFilePath = tempDir.resolve("test-students.dat");
        handler = new StudentDataStreamHandler(tempFilePath.toString());
    }

    @Test
    @DisplayName("Given new file, when readAll called, then return empty list")
    void givenNewFile_whenReadAllCalled_thenReturnEmptyList() throws IOException {
        // Act
        List<Student> result = handler.readAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given multiple Students written to file, when readAll called, then return list with all Students")
    void givenMultipleStudentsWrittenToFile_whenReadAllCalled_thenReturnListWithAllStudents() throws IOException {
        // Arrange
        Student Student1 = new Student("Jan", "Kowalski", "A0001");
        Student Student2 = new Student("Maria", "Nowak", "A0002");
        handler.write(Student1);
        handler.write(Student2);

        // Act
        List<Student> result = handler.readAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().map(Student::getFirstName).anyMatch(name -> name.equals("Jan")));
        assertTrue(result.stream().map(Student::getFirstName).anyMatch(name -> name.equals("Maria")));
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
    @DisplayName("When Student written and read, then all fields match")
    void whenStudentWrittenAndRead_thenAllFieldsMatch() throws IOException {
        // Arrange
        Student originalStudent = new Student("Jan", "Kowalski", "A0001");

        // Act
        handler.write(originalStudent);
        List<Student> result = handler.readAll();

        // Assert
        assertEquals(1, result.size());
        Student readStudent = result.get(0);
        assertEquals(originalStudent.getId(), readStudent.getId());
        assertEquals(originalStudent.getFirstName(), readStudent.getFirstName());
        assertEquals(originalStudent.getLastName(), readStudent.getLastName());
        assertEquals(originalStudent.getAlbum(), readStudent.getAlbum());
    }

    @Test
    @DisplayName("Given empty file, when write called, then file is created")
    void givenEmptyFile_whenWriteCalled_thenFileIsCreated() throws IOException {
        // Arrange
        Student Student = new Student("Jan", "Kowalski", "A0001");

        // Act
        handler.write(Student);

        // Assert
        assertTrue(tempFilePath.toFile().exists());
        assertTrue(tempFilePath.toFile().length() > 0);
    }
}