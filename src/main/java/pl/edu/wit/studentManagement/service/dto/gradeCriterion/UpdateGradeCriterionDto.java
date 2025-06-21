package pl.edu.wit.studentManagement.service.dto.gradeCriterion;

/**
 * Data Transfer Object used for updating an existing grade criterion.
 *
 * @author Michał Zawadzki
 */
public class UpdateGradeCriterionDto {
    /**
     * The updated name of the grade criterion.
     */
    private final String name;

    /**
     * The updated maximum points for the criterion.
     * Nullable to allow partial updates.
     */
    private final Byte maxPoints;

    /**
     * Constructs an UpdateGradeCriterionDto.
     *
     * @param name      the updated name
     * @param maxPoints the updated maximum points (nullable)
     */
    public UpdateGradeCriterionDto(String name, Byte maxPoints) {
        this.name = name;
        this.maxPoints = maxPoints;
    }

    public String getName() {
        return name;
    }

    public Byte getMaxPoints() {
        return maxPoints;
    }
}
