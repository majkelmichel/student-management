package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.wit.studentManagement.service.dto.grade.GradeDto;
import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixDto;
import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixRowDto;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit test suite for {@link GradeQueryService}, verifying the generation of grade matrices
 * for a given subject and student group.
 *
 * @author Michał Zawadzki
 */
class GradeQueryServiceTest {

    private Dao<Student> studentDao;
    private Dao<Grade> gradeDao;
    private Dao<GradeCriterion> gradeCriterionDao;
    private GradeQueryService gradeQueryService;

    @BeforeEach
    void setUp() {
        studentDao = (Dao<Student>) mock(Dao.class);
        gradeDao = (Dao<Grade>) mock(Dao.class);
        gradeCriterionDao = (Dao<GradeCriterion>) mock(Dao.class);
        gradeQueryService = new GradeQueryService(gradeDao, studentDao, gradeCriterionDao);
    }

    @Test
    @DisplayName("Given valid subject and group, when getGradeMatrixForSubjectAndGroup is called, then return correct matrix")
    void givenValidSubjectAndGroup_whenGetGradeMatrixForSubjectAndGroup_thenReturnCorrectMatrix() {
        // Arrange
        UUID subjectId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();

        Student student = new Student("Alice", "Smith", "A0001");
        student.setStudentGroupId(groupId);
        UUID studentId = student.getId();

        GradeCriterion criterion = new GradeCriterion("Midterm", (byte) 100, subjectId);
        UUID criterionId = criterion.getId();

        Grade grade = new Grade(subjectId, criterionId, studentId, (byte) 85);
        UUID gradeId = grade.getId();

        when(studentDao.getAll()).thenReturn(List.of(student));
        when(gradeCriterionDao.getAll()).thenReturn(List.of(criterion));
        when(gradeDao.getAll()).thenReturn(List.of(grade));

        // Act
        GradeMatrixDto matrix = gradeQueryService.getGradeMatrixForSubjectAndGroup(subjectId, groupId);

        // Assert
        assertEquals(List.of("Midterm"), matrix.getCriteriaNames());
        assertEquals(1, matrix.getRows().size());

        GradeMatrixRowDto row = matrix.getRows().get(0);
        assertEquals(studentId, row.getStudentId());
        assertEquals("Alice Smith", row.getStudentName());

        List<GradeDto> grades = row.getGrades();
        assertEquals(1, grades.size());
        GradeDto dto = grades.get(0);
        assertNotNull(dto);
        assertEquals(gradeId, dto.getId());
        assertEquals(subjectId, dto.getSubjectId());
        assertEquals(criterionId, dto.getGradeCriterionId());
        assertEquals(studentId, dto.getStudentId());
        assertEquals(85, dto.getGrade());

        verify(studentDao).getAll();
        verify(gradeCriterionDao).getAll();
        verify(gradeDao).getAll();
    }

    @DisplayName("Given no students in the group, when getGradeMatrixForSubjectAndGroup is called, then return empty rows")
    @Test
    void givenNoStudentsInGroup_whenGetGradeMatrixForSubjectAndGroup_thenReturnEmptyRows() {
        // Arrange
        UUID subjectId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();

        GradeCriterion criterion = new GradeCriterion("Exam", (byte) 100, subjectId);

        when(studentDao.getAll()).thenReturn(List.of());
        when(gradeCriterionDao.getAll()).thenReturn(List.of(criterion));
        when(gradeDao.getAll()).thenReturn(List.of());

        // Act
        GradeMatrixDto matrix = gradeQueryService.getGradeMatrixForSubjectAndGroup(subjectId, groupId);

        // Assert
        assertEquals(List.of("Exam"), matrix.getCriteriaNames());
        assertTrue(matrix.getRows().isEmpty());

        verify(studentDao).getAll();
        verify(gradeCriterionDao).getAll();
        verify(gradeDao).getAll();
    }

    @Test
    @DisplayName("Given no criteria for subject, when getGradeMatrixForSubjectAndGroup is called, then return empty criteria and grades")
    void givenNoCriteriaForSubject_whenGetGradeMatrixForSubjectAndGroup_thenReturnEmptyCriteriaAndGrades() {
        // Arrange
        UUID subjectId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();

        Student student = new Student("John", "Doe", "X1234");
        student.setStudentGroupId(groupId);
        UUID studentId = student.getId();

        when(studentDao.getAll()).thenReturn(List.of(student));
        when(gradeCriterionDao.getAll()).thenReturn(List.of());
        when(gradeDao.getAll()).thenReturn(List.of());

        // Act
        GradeMatrixDto matrix = gradeQueryService.getGradeMatrixForSubjectAndGroup(subjectId, groupId);

        // Assert
        assertTrue(matrix.getCriteriaNames().isEmpty());
        assertEquals(1, matrix.getRows().size());

        GradeMatrixRowDto row = matrix.getRows().get(0);
        assertEquals(studentId, row.getStudentId());
        assertEquals("John Doe", row.getStudentName());
        assertTrue(row.getGrades().isEmpty());

        verify(studentDao).getAll();
        verify(gradeCriterionDao).getAll();
        verify(gradeDao).getAll();
    }
}
