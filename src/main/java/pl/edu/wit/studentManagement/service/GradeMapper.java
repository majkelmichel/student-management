package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.grade.GradeDto;

class GradeMapper {
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
