package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a subject entity with a unique identifier and a name.
 * <p>
 * Implements {@link Serializable} to allow object serialization.
 *
 * @author Michał Zawadzki
 */
class Subject extends Entity {
    /**
     * The unique identifier for this subject
     */
    private final UUID id;
    /**
     * The name of this subject
     */
    private String name;

    /**
     * Constructs a new {@code Subject} with the specified ID and name.
     *
     * @param id   the unique identifier for this subject
     * @param name the subject name (e.g., "Mathematics")
     */
    public Subject(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructs a new {@code Subject} with the specified name.
     * A unique UUID is generated automatically.
     *
     * @param name the subject name (e.g., "Mathematics")
     */
    Subject(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    UUID getId() {
        return id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    /**
     * Validates the subject fields to ensure they meet the required criteria.
     * <p>
     * The subject name must not be null or empty.
     *
     * @throws ValidationException if the name is null or empty
     */
    void validate() throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("subject.name.empty");
        }
    }
}
