package pl.edu.wit.studentManagement.service.dto.gradeCriterion;

/**
 * Data Transfer Object used for creating a new grade criterion.
 *
 * @author Michał Zawadzki
 */
public class CreateGradeCriterionDto {
    /**
     * The name of the grade criterion (e.g., "Final Exam", "Project").
     */
    private final String name;

    /**
     * The maximum points possible for this criterion.
     */
    private final byte maxPoints;

    /**
     * Constructs a new CreateCriterionDto.
     *
     * @param name      the name of the criterion
     * @param maxPoints the maximum points achievable
     */
    public CreateGradeCriterionDto(String name, byte maxPoints) {
        this.name = name;
        this.maxPoints = maxPoints;
    }

    public String getName() {
        return name;
    }

    public byte getMaxPoints() {
        return maxPoints;
    }
}
