package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectWithGradeCriteriaDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting {@link Subject} entities to various Data Transfer Objects (DTOs).
 * <p>
 * Provides static methods to transform domain entities into lightweight DTO representations
 * for use in different service or presentation layers.
 *
 * @author Michał Zawadzki
 */
class SubjectMapper {

    /**
     * Converts a {@link Subject} entity to a simple {@link SubjectDto}.
     *
     * @param subject the {@code Subject} entity to convert
     * @return the corresponding {@code SubjectDto} containing basic subject data
     */
    static SubjectDto toDto(Subject subject) {
        return new SubjectDto(
                subject.getId(),
                subject.getName()
        );
    }

    /**
     * Converts a {@link Subject} entity along with its associated grade criteria
     * into a {@link SubjectWithGradeCriteriaDto}.
     *
     * @param subject       the {@code Subject} entity to convert
     * @param gradeCriteria list of {@code GradeCriterion} associated with the subject
     * @return a {@code SubjectWithGradeCriteriaDto} containing subject data and its grade criteria
     */
    static SubjectWithGradeCriteriaDto toDtoWithGradeCriteria(Subject subject, List<GradeCriterion> gradeCriteria) {
        return new SubjectWithGradeCriteriaDto(
                subject.getId(),
                subject.getName(),
                gradeCriteria.stream().map(GradeCriterionMapper::toDto).collect(Collectors.toList())
        );
    }
}
