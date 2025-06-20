package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Abstract Data Access Object (DAO) defining the contract for basic CRUD operations
 * on entities of type T identified by UUID.
 *
 * @param <T> the type of entity this DAO manages
 *
 * @author Michał Zawadzki
 */
abstract class Dao<T> {

    /**
     * Retrieves an entity by its unique identifier.
     *
     * @param id UUID of the entity
     * @return Optional containing the entity if found, or empty if not found
     */
    abstract Optional<T> get(UUID id);

    /**
     * Retrieves all entities.
     *
     * @return List of all entities
     */
    abstract List<T> getAll();

    /**
     * Saves a new entity.
     *
     * @param t the entity to save
     * @throws ValidationException if the entity fails validation
     */
    abstract void save(T t) throws ValidationException;

    /**
     * Updates an existing entity.
     *
     * @param t the entity with updated data
     * @throws ValidationException if the entity fails validation
     */
    abstract void update(T t) throws ValidationException;

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id UUID of the entity to delete
     * @return true if the entity was deleted, false if no entity was found with the given id
     */
    abstract boolean delete(UUID id);
}
