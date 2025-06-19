package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectWithGradeCriteriaDto;

import java.util.List;
import java.util.stream.Collectors;

class SubjectMapper {
    static SubjectDto toDto(Subject subject) {
        return new SubjectDto(
                subject.getId(),
                subject.getName()
        );
    }

    static SubjectWithGradeCriteriaDto toDtoWithGradeCriteria(Subject subject, List<GradeCriterion> gradeCriteria) {
        return new SubjectWithGradeCriteriaDto(
                subject.getId(),
                subject.getName(),
                gradeCriteria.stream().map(GradeCriterionMapper::toDto).collect(Collectors.toList())
        );
    }
}
