package pl.edu.wit.studentManagement.service.dto.grade;

import java.util.UUID;

public class GradeDto {
    private final UUID id;
    private final UUID subjectId;
    private final UUID gradeCriterionId;
    private final UUID studentId;
    private final byte grade;

    public GradeDto(UUID id, UUID subjectId, UUID gradeCriterionId, UUID studentId, byte grade) {
        this.id = id;
        this.subjectId = subjectId;
        this.gradeCriterionId = gradeCriterionId;
        this.studentId = studentId;
        this.grade = grade;
    }

    public UUID getId() {
        return id;
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
