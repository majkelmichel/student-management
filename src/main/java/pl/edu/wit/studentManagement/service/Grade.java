package pl.edu.wit.studentManagement.service;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a Grade assigned to a student for a specific subject and grading criterion.
 * <p>
 * This class is serializable for persistence and contains identifiers linking
 * to the subject, grade criterion, and student, along with the grade value.
 * The unique {@code id} identifies the grade instance.
 *
 * @author Michał Zawadzki
 */
class Grade implements Serializable {
    private final UUID id;
    private UUID subjectId;
    private UUID gradeCriterionId;
    private UUID studentId;
    private byte grade;

    /**
     * Constructs a new Grade with the specified subject, criterion, student, and grade value.
     *
     * @param subjectId        the UUID of the subject
     * @param gradeCriterionId the UUID of the grade criterion
     * @param studentId        the UUID of the student
     * @param grade            the grade value
     */
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
