package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a grading criterion for a subject, defining a maximum number of points and a name.
 * <p>
 * Each GradeCriterion is uniquely identified by a UUID and linked to a subject by its UUID.
 * The class supports validation to ensure the criterion has a valid name and positive maxPoints.
 *
 * @author Michał Zawadzki
 */
class GradeCriterion extends Entity {
    /** The unique identifier of this grade criterion */
    private final UUID id;
    
    /** The name of the grade criterion */
    private String name;
    
    /** The maximum number of points that can be awarded for this criterion */
    private byte maxPoints;
    
    /** The unique identifier of the subject this criterion belongs to */
    private UUID subjectId;

    /**
     * Constructs a GradeCriterion with the specified name, maximum points, and subject ID.
     *
     * @param name      the name of the criterion
     * @param maxPoints the maximum achievable points for this criterion
     * @param subjectId the UUID of the subject this criterion belongs to
     */
    GradeCriterion(String name, byte maxPoints, UUID subjectId) {
        this.subjectId = subjectId;
        this.id = UUID.randomUUID();
        this.name = name;
        this.maxPoints = maxPoints;
    }

    GradeCriterion(UUID id, String name, byte maxPoints, UUID subjectId) {
        this.id = id;
        this.name = name;
        this.maxPoints = maxPoints;
        this.subjectId = subjectId;
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

    byte getMaxPoints() {
        return maxPoints;
}

    void setMaxPoints(byte maxPoints) {
        this.maxPoints = maxPoints;
    }

    /**
     * Validates the GradeCriterion fields.
     *
     * @throws ValidationException if the name is null/empty or maxPoints is non-positive
     */
    void validate() throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("criterion.name.empty");
        }
        if (maxPoints <= 0) {
            throw new ValidationException("criterion.maxPoints.invalid");
        }
    }

    UUID getSubjectId() {
        return subjectId;
    }

    void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }
}
