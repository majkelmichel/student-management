package pl.edu.wit.studentManagement.service.dto.gradeCriterion;

import java.util.UUID;

public class GradeCriterionDto {
    private final UUID id;
    private final String name;
    private final byte maxPoints;

    public GradeCriterionDto(UUID id, String name, byte maxPoints) {
        this.id = id;
        this.name = name;
        this.maxPoints = maxPoints;
    }

    public String getName() {
        return name;
    }

    public byte getMaxPoints() {
        return maxPoints;
    }

    public UUID getId() {
        return id;
    }
}
