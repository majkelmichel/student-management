package pl.edu.wit.studentManagement.service.dto.gradeMatrix;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object representing a single row in the grade matrix,
 * corresponding to one student and their grades for each criterion.
 *
 * @author Michał Zawadzki
 */
public class GradeMatrixRowDto {
    private final UUID studentId;
    private final String studentName;
    private final List<Byte> grades;

    /**
     * Constructs a GradeMatrixRowDto.
     *
     * @param studentId the unique identifier of the student
     * @param studentName the full name of the student
     * @param grades the list of grades aligned with the criteria names; can contain nulls for missing grades
     */
    public GradeMatrixRowDto(UUID studentId, String studentName, List<Byte> grades) {
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
     * @return list of grades (Byte), possibly containing nulls
     */
    public List<Byte> getGrades() {
        return grades;
    }
}
