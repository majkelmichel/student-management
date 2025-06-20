package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

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
class GradeCriterion implements Serializable {
    private final UUID id;
    private String name;
    private byte maxPoints;
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
