package pl.edu.wit.studentManagement.service;

import java.io.Serializable;
import java.util.UUID;

class Grade implements Serializable {
    private final UUID id;
    private UUID subjectId;
    private UUID gradeCriterionId;
    private UUID studentId;
    private byte grade;

    Grade(UUID subjectId, UUID gradeCriterionId, UUID studentId, byte grade) {
        this.id = UUID.randomUUID();
        this.subjectId = subjectId;
        this.gradeCriterionId = gradeCriterionId;
        this.studentId = studentId;
        this.grade = grade;
    }

    UUID getSubjectId() {
        return subjectId;
    }

    void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    UUID getGradeCriterionId() {
        return gradeCriterionId;
    }

    void setGradeCriterionId(UUID gradeCriterionId) {
        this.gradeCriterionId = gradeCriterionId;
    }

    UUID getStudentId() {
        return studentId;
    }

    void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    byte getGrade() {
        return grade;
    }

    void setGrade(byte grade) {
        this.grade = grade;
    }

    UUID getId() {
        return id;
    }
}
