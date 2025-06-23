package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a student group with a unique identifier, code, specialization, and description.
 * <p>
 * Provides getters and setters for mutable fields and ensures the group's data integrity through validation.
 * <p>
 * Implements {@link Serializable} to allow object serialization.
 *
 * @author Michał Zawadzki
 */
class StudentGroup extends Entity {
    /**
     * Unique identifier for the student group
     */
    private final UUID id;
    /**
     * Code representing the student group (e.g., "CS2023")
     */
    private String code;
    /**
     * Field of study or specialization for this group
     */
    private String specialization;
    /**
     * Optional description providing additional information about the group
     */
    private String description;

    /**
     * Constructs a new {@code StudentGroup} with the specified code, specialization, and description.
     * A unique UUID is generated automatically.
     *
     * @param code           the group code (e.g., "IZ06IO1")
     * @param specialization the group's specialization (e.g., "Inżynieria Oprogramowania")
     * @param description    additional description about the group
     */
    StudentGroup(String code, String specialization, String description) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.specialization = specialization;
        this.description = description;
    }

    public StudentGroup(UUID id, String code, String specialization, String description) {
        this.id = id;
        this.code = code;
        this.specialization = specialization;
        this.description = description;
    }

    UUID getId() {
        return id;
    }

    String getCode() {
        return code;
    }

    void setCode(String code) {
        this.code = code;
    }

    String getSpecialization() {
        return specialization;
    }

    void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Validates the student group fields to ensure they meet the required criteria.
     * <p>
     * The group code and specialization must not be null or empty.
     * Description can be null or empty.
     *
     * @throws ValidationException if code or specialization is null or empty
     */
    void validate() throws ValidationException {
        if (code == null || code.trim().isEmpty()) {
            throw new ValidationException("studentGroup.code.empty");
        }
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new ValidationException("studentGroup.specialization.empty");
        }
    }
}
