package pl.edu.wit.studentManagement.service.dto.grade;

import java.util.UUID;

/**
 * Data Transfer Object representing a grade assigned to a student.
 * <p>
 * Contains identifiers for the grade itself, related subject, grading criterion,
 * and student, along with the grade value.
 *
 * @author Michał Zawadzki
 */
public class GradeDto {
    private final UUID id;
    private final UUID subjectId;
    private final UUID gradeCriterionId;
    private final UUID studentId;
    private final byte grade;

    /**
     * Constructs a GradeDto.
     *
     * @param id               the unique ID of the grade
     * @param subjectId        the subject's unique ID
     * @param gradeCriterionId the grading criterion's unique ID
     * @param studentId        the student's unique ID
     * @param grade            the grade value
     */
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
