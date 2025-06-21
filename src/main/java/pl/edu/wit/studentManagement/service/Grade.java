package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a Grade assigned to a student for a specific subject and grading criterion.
 * <p>
 * This class is serializable for persistence and contains identifiers linking
 * to the subject, grade criterion, and student, along with the grade value.
 * The unique {@code id} identifies the grade instance.
 * <p>
 * Use {@link #validate()} to ensure the object is in a consistent state before persisting.
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

    /**
     * Validates the state of this Grade instance.
     * <p>
     * Ensures that the subject, grade criterion, and student IDs are not null,
     * and that the grade value is within the valid range of 0 to 100.
     *
     * @throws ValidationException if any of the fields are invalid
     */
    void validate() throws ValidationException {
        if (subjectId == null) {
            throw new ValidationException("grade.subject.empty");
        }
        if (gradeCriterionId == null) {
            throw new ValidationException("grade.gradeCriterion.empty");
        }
        if (studentId == null) {
            throw new ValidationException("grade.student.empty");
        }
        if (grade < 0 || grade > 100) {
            throw new ValidationException("grade.value.outOfRange");
        }
    }
}
