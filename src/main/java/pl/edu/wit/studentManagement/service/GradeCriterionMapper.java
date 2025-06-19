package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;

class GradeCriterionMapper {
    static GradeCriterionDto toDto(GradeCriterion gradeCriterion) {
        return new GradeCriterionDto(
                gradeCriterion.getId(),
                gradeCriterion.getName(),
                gradeCriterion.getMaxPoints()
        );
    }
}
