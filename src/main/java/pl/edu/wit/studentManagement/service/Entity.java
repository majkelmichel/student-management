package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.util.UUID;

/**
 * Abstract base class for all domain entities in the student management system.
 * <p>
 * Provides common functionality for entity validation and identification.
 *
 * @author Michał Zawadzki
 */
abstract class Entity {
    /**
     * Validates the entity's state.
     * Implementations should check all business rules and constraints.
     *
     * @throws ValidationException if the entity's state is invalid
     */
    abstract void validate() throws ValidationException;

    /**
     * Returns the unique identifier of this entity.
     *
     * @return the UUID that uniquely identifies this entity
     */
    abstract UUID getId();
}
