package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.Serializable;
import java.util.UUID;

class GradeCriterion implements Serializable {
    private final UUID id;
    private String name;
    private byte maxPoints;
    private UUID subjectId;

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
