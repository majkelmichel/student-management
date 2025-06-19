package pl.edu.wit.studentManagement.service.dto.gradeCriterion;

public class CreateCriterionDto {
    private final String name;
    private final byte maxPoints;

    public CreateCriterionDto(String name, byte maxPoints) {
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
