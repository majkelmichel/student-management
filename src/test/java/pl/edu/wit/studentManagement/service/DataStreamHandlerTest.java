package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DataStreamHandler class verifying data persistence operations.
 * Uses TestDataStreamHandler and TestEntity helper classes.
 *
 * @see DataStreamHandler
 * @see Entity
 * @author Michał Zawadzki
 */
@DisplayName("DataStreamHandler Test Suite")
class DataStreamHandlerTest {
    
    @TempDir
    Path tempDir;
    
    private TestDataStreamHandler handler;
    private String filePath;
    
    @BeforeEach
    void setUp() {
        filePath = tempDir.resolve("test.dat").toString();
        handler = new TestDataStreamHandler(filePath);
    }
    
    @Test
    @DisplayName("Given new file, when readAll called, then returns empty list")
    void givenNewFile_whenReadAllCalled_thenReturnsEmptyList() throws IOException {
        // Act
        List<TestEntity> result = handler.readAll();
        
        // Assert
        assertTrue(result.isEmpty());
    }
    
    @Test
    @DisplayName("Given entity, when write called, then entity is persisted")
    void givenEntity_whenWriteCalled_thenEntityIsPersisted() throws IOException {
        // Arrange
        TestEntity entity = new TestEntity("Test");
        
        // Act
        handler.write(entity);
        List<TestEntity> result = handler.readAll();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(entity.getId(), result.get(0).getId());
        assertEquals(entity.getName(), result.get(0).getName());
    }
    
    @Test
    @DisplayName("Given existing entity, when update called, then entity is updated")
    void givenExistingEntity_whenUpdateCalled_thenEntityIsUpdated() throws IOException {
        // Arrange
        TestEntity entity = new TestEntity("Test");
        handler.write(entity);
        
        // Act 
        TestEntity updatedEntity = new TestEntity(entity.getId(), "Updated");
        handler.update(updatedEntity);
        
        // Assert
        List<TestEntity> result = handler.readAll();
        assertEquals(1, result.size());
        assertEquals("Updated", result.get(0).getName());
    }
    
    @Test
    @DisplayName("Given non-existing entity, when update called, then throws IOException")
    void givenNonExistingEntity_whenUpdateCalled_thenThrowsIOException() {
        // Arrange
        TestEntity entity = new TestEntity("Test");
        
        // Act & Assert
        assertThrows(IOException.class, () -> handler.update(entity));
    }
    
    @Test
    @DisplayName("Given existing entity, when deleteById called, then entity is removed")
    void givenExistingEntity_whenDeleteByIdCalled_thenEntityIsRemoved() throws IOException {
        // Arrange
        TestEntity entity = new TestEntity("Test");
        handler.write(entity);
        
        // Act
        handler.deleteById(entity.getId());
        
        // Assert
        List<TestEntity> result = handler.readAll();
        assertTrue(result.isEmpty());
    }
    
    @Test
    @DisplayName("Given non-existing entity, when deleteById called, then throws IOException")
    void givenNonExistingEntity_whenDeleteByIdCalled_thenThrowsIOException() {
        // Act & Assert
        assertThrows(IOException.class, () -> handler.deleteById(UUID.randomUUID()));
    }
    
    // Helper concrete implementation for testing
    private static class TestDataStreamHandler extends DataStreamHandler<TestEntity> {
        TestDataStreamHandler(String filePath) {
            super(filePath);
        }
        
        @Override
        TestEntity readObject(DataInputStream in) throws IOException {
            UUID id = readUuid(in);
            String name = in.readUTF();
            return new TestEntity(id, name);
        }
        
        @Override
        void writeObject(DataOutputStream out, TestEntity object) throws IOException {
            writeUuid(object.getId(), out);
            out.writeUTF(object.getName());
        }
    }
    
    // Helper entity class for testing
    private static class TestEntity extends Entity {
        private final UUID id;
        private final String name;
        
        TestEntity(String name) {
            this.id = UUID.randomUUID();
            this.name = name;
        }
        
        TestEntity(UUID id, String name) {
            this.id = id;
            this.name = name;
        }
        
        @Override
        void validate() throws ValidationException {
            if (name == null || name.isEmpty()) {
                throw new ValidationException("Name cannot be empty");
            }
        }
        
        @Override
        UUID getId() {
            return id;
        }
        
        String getName() {
            return name;
        }
    }
}