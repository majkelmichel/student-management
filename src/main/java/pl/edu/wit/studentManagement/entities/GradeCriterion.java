package pl.edu.wit.studentManagement.entities;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.UUID;

public class GradeCriterion {
    private final UUID id;
    private String name;
    private byte maxPoints;

    public GradeCriterion(String name, byte maxPoints) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.maxPoints = maxPoints;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(byte maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void validate() throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("criterion.name.empty");
        }
        if (maxPoints <= 0) {
            throw new ValidationException("criterion.maxPoints.invalid");
        }
    }
}
