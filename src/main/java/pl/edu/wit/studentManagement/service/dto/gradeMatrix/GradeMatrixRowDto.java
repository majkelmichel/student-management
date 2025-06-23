package pl.edu.wit.studentManagement.service.dto.gradeMatrix;

import pl.edu.wit.studentManagement.service.dto.grade.GradeDto;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object representing a single row in the grade matrix,
 * corresponding to one student and their grades for each criterion.
 *
 * @author Michał Zawadzki
 */
public class GradeMatrixRowDto {
    /**
     * The unique identifier of the student.
     */
    private final UUID studentId;
    /**
     * The full name of the student.
     */
    private final String studentName;
    /**
     * The list of grades for each criterion, can contain nulls for missing grades.
     */
    private final List<GradeDto> grades;

    /**
     * Constructs a GradeMatrixRowDto.
     *
     * @param studentId the unique identifier of the student
     * @param studentName the full name of the student
     * @param grades the list of grades aligned with the criteria names; can contain nulls for missing grades
     */
    public GradeMatrixRowDto(UUID studentId, String studentName, List<GradeDto> grades) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.grades = grades;
    }

    /**
     * Returns the student's unique identifier.
     *
     * @return UUID of the student
     */
    public UUID getStudentId() {
        return studentId;
    }

    /**
     * Returns the full name of the student.
     *
     * @return student's full name
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Returns the list of grades for the student, aligned by criterion.
     * May contain null values for missing grades.
     *
     * @return list of grades ({@link GradeDto}), possibly containing nulls
     */
    public List<GradeDto> getGrades() {
        return grades;
    }
}
