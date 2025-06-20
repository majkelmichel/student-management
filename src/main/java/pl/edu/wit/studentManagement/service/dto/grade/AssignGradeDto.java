package pl.edu.wit.studentManagement.service.dto.grade;

import java.util.UUID;

/**
 * Data Transfer Object for assigning a grade to a student.
 * <p>
 * This DTO contains all necessary information required to assign a grade
 * to a student in a specific subject based on a grading criterion.
 *
 * @author Michał Zawadzki
 */
public class AssignGradeDto {

    /**
     * The unique identifier of the subject the grade is associated with.
     */
    private final UUID subjectId;

    /**
     * The unique identifier of the grading criterion (e.g., exam, project, etc.).
     */
    private final UUID gradeCriterionId;

    /**
     * The unique identifier of the student receiving the grade.
     */
    private final UUID studentId;

    /**
     * The grade value to assign.
     * <p>
     * Typically expected to be within a valid range defined by the system (e.g., 2–5 or 0–100).
     */
    private final byte grade;

    /**
     * Constructs a new AssignGradeDto.
     *
     * @param subjectId         the subject's unique ID
     * @param gradeCriterionId  the grade criterion's unique ID
     * @param studentId         the student's unique ID
     * @param grade             the grade value to assign
     */
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
