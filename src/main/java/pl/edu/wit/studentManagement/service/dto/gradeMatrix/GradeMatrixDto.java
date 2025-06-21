package pl.edu.wit.studentManagement.service.dto.gradeMatrix;

import java.util.List;

/**
 * Data Transfer Object representing a matrix of grades for a given subject and group.
 * <p>
 * The matrix consists of a list of grade criteria names as columns, and a list of rows
 * where each row corresponds to a student and their grades for each criterion.
 *
 * @author Michał Zawadzki
 */
public class GradeMatrixDto {
    private final List<String> criteriaNames;
    private final List<GradeMatrixRowDto> rows;

    /**
     * Constructs a GradeMatrixDto with specified criteria names and rows.
     *
     * @param criteriaNames     the list of grade criteria names (e.g., "Test 1", "Homework")
     * @param rows              the list of grade matrix rows, each representing a student's grades
     */
    public GradeMatrixDto(List<String> criteriaNames, List<GradeMatrixRowDto> rows) {
        this.criteriaNames = criteriaNames;
        this.rows = rows;
    }

    /**
     * Returns the list of grade criteria names (columns).
     *
     * @return list of criteria names
     */
    public List<String> getCriteriaNames() {
        return criteriaNames;
    }

    /**
     * Returns the list of rows representing students and their grades.
     *
     * @return list of GradeMatrixRowDto objects
     */
    public List<GradeMatrixRowDto> getRows() {
        return rows;
    }
}
