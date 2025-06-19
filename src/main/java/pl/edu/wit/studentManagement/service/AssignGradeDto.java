package pl.edu.wit.studentManagement.service;

import java.util.UUID;

public class AssignGradeDto {
    private final UUID subjectId;
    private final UUID gradeCriterionId;
    private final UUID studentId;
    private final byte grade;

    public AssignGradeDto(UUID subjectId, UUID gradeCriterionId, UUID studentId, byte grade) {
        this.subjectId = subjectId;
        this.gradeCriterionId = gradeCriterionId;
        this.studentId = studentId;
        this.grade = grade;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public UUID getGradeCriterionId() {
        return gradeCriterionId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public byte getGrade() {
        return grade;
    }
}
