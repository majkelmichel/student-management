package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Generic Data Access Object (DAO) class that provides CRUD operations for entities.
 * This class handles basic database operations using a DataStreamHandler for persistence.
 *
 * @param <T> the type of entity this DAO manages, must extend Entity class
 *
 * @author Michał Zawadzki
 */
class Dao<T extends Entity> {
    /**
     * Handler responsible for reading and writing entity data to persistent storage.
     * This field manages all data stream operations for the specific entity type.
     */
    DataStreamHandler<T> dataStreamHandler;

    /**
     * Constructs a new DAO with the specified data stream handler.
     *
     * @param dataStreamHandler the handler responsible for entity persistence operations
     */
    Dao(DataStreamHandler<T> dataStreamHandler) {
        this.dataStreamHandler = dataStreamHandler;
    }

    /**
     * Retrieves an entity by its unique identifier.
     *
     * @param id UUID of the entity
     * @return Optional containing the entity if found, or empty if not found
     */
    Optional<T> get(UUID id) {
        try {
            return dataStreamHandler.readAll().stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all entities.
     *
     * @return List of all entities
     */
    List<T> getAll() {
        try {
            return dataStreamHandler.readAll();
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Saves a new entity.
     *
     * @param t the entity to save
     * @throws ValidationException if the entity fails validation
     */
    void save(T t) throws ValidationException {
        t.validate();
        try {
            dataStreamHandler.write(t);
        } catch (Exception e) {
            throw new ValidationException(String.format("%s.save.failed", t.getClass().getName()));
        }
    }

    /**
     * Updates an existing entity.
     *
     * @param t the entity with updated data
     * @throws ValidationException if the entity fails validation
     */
    void update(T t) throws ValidationException {
        t.validate();
        try {
            dataStreamHandler.update(t);
        } catch (Exception e) {
            throw new ValidationException(String.format("%s.update.failed", t.getClass().getName()));
        }
    }

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id UUID of the entity to delete
     * @return true if the entity was deleted, false if no entity was found with the given id
     */
    boolean delete(UUID id) {
        try {
            dataStreamHandler.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
