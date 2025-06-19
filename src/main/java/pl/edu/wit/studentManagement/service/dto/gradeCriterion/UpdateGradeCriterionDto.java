package pl.edu.wit.studentManagement.service.dto.gradeCriterion;

public class UpdateGradeCriterionDto {
    private final String name;
    private final Byte maxPoints;

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
