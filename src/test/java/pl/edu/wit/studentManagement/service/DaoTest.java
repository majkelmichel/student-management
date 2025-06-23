package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for the Dao class that verifies CRUD operations' functionality.
 * These tests ensure the proper behavior of data access operations using mocked DataStreamHandler.
 * The test suite covers entity retrieval, creation, update, and deletion scenarios.
 *
 * @author Michał Zawadzki
 */
@DisplayName("Dao Test Suite")
class DaoTest {
    private DataStreamHandler<TestEntity> dataStreamHandler;
    private Dao<TestEntity> dao;
    private TestEntity testEntity;

    @BeforeEach
    void setUp() {
        dataStreamHandler = mock(DataStreamHandler.class);
        dao = new Dao<>(dataStreamHandler);
        testEntity = new TestEntity(UUID.randomUUID());
    }

    @Test
    @DisplayName("Given valid entity, when get by id, then return Optional with entity")
    void givenValidEntity_whenGetById_thenReturnOptionalWithEntity() throws IOException {
        // Arrange
        List<TestEntity> entities = List.of(testEntity);
        when(dataStreamHandler.readAll()).thenReturn(entities);

        // Act
        Optional<TestEntity> result = dao.get(testEntity.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testEntity.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Given non-existent id, when get by id, then return empty Optional")
    void givenNonExistentId_whenGetById_thenReturnEmptyOptional() throws IOException {
        // Arrange
        when(dataStreamHandler.readAll()).thenReturn(List.of());

        // Act
        Optional<TestEntity> result = dao.get(UUID.randomUUID());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("When getAll called, then return list of all entities")
    void whenGetAllCalled_thenReturnListOfAllEntities() throws IOException {
        // Arrange
        List<TestEntity> entities = List.of(testEntity);
        when(dataStreamHandler.readAll()).thenReturn(entities);

        // Act
        List<TestEntity> result = dao.getAll();

        // Assert
        assertEquals(entities.size(), result.size());
        assertEquals(entities.get(0).getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Given valid entity, when save, then write to stream")
    void givenValidEntity_whenSave_thenWriteToStream() throws IOException, ValidationException {
        // Act
        dao.save(testEntity);

        // Assert
        verify(dataStreamHandler).write(testEntity);
    }

    @Test
    @DisplayName("Given invalid entity, when save, then throw ValidationException")
    void givenInvalidEntity_whenSave_thenThrowValidationException() throws IOException {
        // Arrange
        TestEntity invalidEntity = new TestEntity(null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> dao.save(invalidEntity));
    }

    @Test
    @DisplayName("Given valid entity, when update, then update in stream")
    void givenValidEntity_whenUpdate_thenUpdateInStream() throws IOException, ValidationException {
        // Act
        dao.update(testEntity);

        // Assert
        verify(dataStreamHandler).update(testEntity);
    }

    @Test
    @DisplayName("Given invalid entity, when update, then throw ValidationException")
    void givenInvalidEntity_whenUpdate_thenThrowValidationException() throws IOException {
        // Arrange
        TestEntity invalidEntity = new TestEntity(null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> dao.update(invalidEntity));
    }

    @Test
    @DisplayName("Given existing id, when delete, then return true")
    void givenExistingId_whenDelete_thenReturnTrue() throws IOException {
        // Act
        boolean result = dao.delete(testEntity.getId());

        // Assert
        assertTrue(result);
        verify(dataStreamHandler).deleteById(testEntity.getId());
    }

    @Test
    @DisplayName("Given non-existent id, when delete, then return false")
    void givenNonExistentId_whenDelete_thenReturnFalse() throws IOException {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        doThrow(IOException.class).when(dataStreamHandler).deleteById(nonExistentId);

        // Act
        boolean result = dao.delete(nonExistentId);

        // Assert
        assertFalse(result);
    }

    // Test helper class
    private static class TestEntity extends Entity {
        private final UUID id;

        TestEntity(UUID id) {
            this.id = id;
        }

        @Override
        void validate() throws ValidationException {
            if (id == null) {
                throw new ValidationException("Invalid ID");
            }
        }

        @Override
        UUID getId() {
            return id;
        }
    }
}