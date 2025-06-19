package pl.edu.wit.studentManagement.service.dto.gradeMatrix;

import java.util.List;
import java.util.UUID;

public class GradeMatrixRowDto {
    private final UUID studentId;
    private final String studentName;
    private final List<Byte> grades;

    public GradeMatrixRowDto(UUID studentId, String studentName, List<Byte> grades) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.grades = grades;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public List<Byte> getGrades() {
        return grades;
    }
}
