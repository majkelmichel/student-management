package pl.edu.wit.studentManagement.service.dto.subject;

import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;

import java.util.List;
import java.util.UUID;

public class SubjectWithGradeCriteriaDto {
    private final UUID id;
    private final String name;
    private final List<GradeCriterionDto> gradeCriteria;

    public SubjectWithGradeCriteriaDto(UUID id, String name, List<GradeCriterionDto> gradeCriteria) {
        this.id = id;
        this.name = name;
        this.gradeCriteria = gradeCriteria;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<GradeCriterionDto> getGradeCriteria() {
        return gradeCriteria;
    }
}
