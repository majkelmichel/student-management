package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.grade.GradeDto;

/**
 * Utility class responsible for mapping {@link Grade} entities to {@link GradeDto} objects.
 * <p>
 * This class provides transformation logic between the internal domain model and the external data transfer object
 * used for presentation or API responses.
 *
 * @author Michał Zawadzki
 */
class GradeMapper {

    /**
     * Converts a {@link Grade} entity to a {@link GradeDto}.
     *
     * @param grade the grade entity to convert
     * @return the corresponding GradeDto
     */
    static GradeDto toDto(Grade grade) {
        return new GradeDto(
                grade.getId(),
                grade.getSubjectId(),
                grade.getGradeCriterionId(),
                grade.getStudentId(),
                grade.getGrade()
        );
    }
}
