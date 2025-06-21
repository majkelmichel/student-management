package pl.edu.wit.studentManagement.service.dto.gradeCriterion;

import java.util.UUID;

/**
 * Data Transfer Object representing a grade criterion.
 *
 * @author Michał Zawadzki
 */
public class GradeCriterionDto {
    /**
     * The unique identifier of the grade criterion.
     */
    private final UUID id;

    /**
     * The name of the grade criterion.
     */
    private final String name;

    /**
     * The maximum points possible for this criterion.
     */
    private final byte maxPoints;

    /**
     * Constructs a GradeCriterionDto.
     *
     * @param id        the unique ID of the criterion
     * @param name      the name of the criterion
     * @param maxPoints the maximum points achievable
     */
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
