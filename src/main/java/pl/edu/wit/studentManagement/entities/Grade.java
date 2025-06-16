package pl.edu.wit.studentManagement.entities;

import java.util.UUID;

public class Grade {
    private final UUID id;
    private UUID subjectId;
    private UUID gradeCriterionId;
    private UUID studentId;
    private byte grade;

    public Grade(UUID subjectId, UUID gradeCriterionId, UUID studentId, byte grade) {
        this.id = UUID.randomUUID();
        this.subjectId = subjectId;
        this.gradeCriterionId = gradeCriterionId;
        this.studentId = studentId;
        this.grade = grade;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public UUID getGradeCriterionId() {
        return gradeCriterionId;
    }

    public void setGradeCriterionId(UUID gradeCriterionId) {
        this.gradeCriterionId = gradeCriterionId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public byte getGrade() {
        return grade;
    }

    public void setGrade(byte grade) {
        this.grade = grade;
    }

    public UUID getId() {
        return id;
    }
}