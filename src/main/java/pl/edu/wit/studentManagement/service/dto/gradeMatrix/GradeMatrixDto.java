package pl.edu.wit.studentManagement.service.dto.gradeMatrix;

import java.util.List;

public class GradeMatrixDto {
    private final List<String> criteriaNames;
    private final List<GradeMatrixRowDto> rows;

    public GradeMatrixDto(List<String> criteriaNames, List<GradeMatrixRowDto> rows) {
        this.criteriaNames = criteriaNames;
        this.rows = rows;
    }

    public List<String> getCriteriaNames() {
        return criteriaNames;
    }

    public List<GradeMatrixRowDto> getRows() {
        return rows;
    }
}
