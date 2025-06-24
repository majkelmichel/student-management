package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.dto.grade.AssignGradeDto;
import pl.edu.wit.studentManagement.service.dto.grade.GradeDto;
import pl.edu.wit.studentManagement.service.dto.grade.UpdateGradeDto;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test suite for {@link GradeService}, verifying methods handling grades.
 *
 * @author Martin Szum
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GradeService Test Suite")
class GradeServiceTest {

    @Mock
    private Dao<Grade> gradeDao;

    @Mock
    private Dao<Student> studentDao;

    @Mock
    private Dao<GradeCriterion> gradeCriterionDao;

    @Mock
    private Dao<Subject> subjectDao;

    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        gradeService = new GradeService(gradeDao, studentDao, gradeCriterionDao, subjectDao);
    }

    @Test
    @DisplayName("Given correct AssignGradeDto, when assignGrade called, then assign grade and return correct DTO")
    void givenCorrectAssignGradeDto_whenAssignGradeCalled_thenAssignCorrectDTO(){
        // Arrange
        Student student = new Student("Jan", "Kowalski", "A0001");
        Subject subject = new Subject("Java");
        GradeCriterion criterion = new GradeCriterion("Egzamin", (byte) 50, subject.getId());

        UUID subjectId = subject.getId();
        UUID gradeCriterionId = criterion.getId();
        UUID studentId = student.getId();
        byte grade = 50;
        AssignGradeDto assignGradeDto = new AssignGradeDto(subjectId, gradeCriterionId, studentId, grade);

        when(studentDao.get(assignGradeDto.getStudentId())).thenReturn(Optional.of(student));
        when(subjectDao.get(assignGradeDto.getSubjectId())).thenReturn(Optional.of(subject));
        when(gradeCriterionDao.get(assignGradeDto.getGradeCriterionId())).thenReturn(Optional.of(criterion));

        // Act
        GradeDto result = assertDoesNotThrow(() -> gradeService.assignGrade(assignGradeDto));

        // Assert
        assertNotNull(result);
        assertEquals(subjectId, result.getSubjectId());
        assertEquals(gradeCriterionId, result.getGradeCriterionId());
        assertEquals(studentId, result.getStudentId());
        assertEquals(grade, result.getGrade());
    }

    @Test
    @DisplayName("Given non-existing student, when assignGrade called, then throw ValidationException")
    void givenNonExistingStudent_whenAssignGradeCalled_thenThrowValidationException() {
        // Arrange
        Subject subject = new Subject("Java");
        GradeCriterion criterion = new GradeCriterion("Egzamin", (byte) 50, subject.getId());

        UUID studentId = UUID.randomUUID();
        AssignGradeDto assignGradeDto = new AssignGradeDto(subject.getId(), criterion.getId(), studentId, (byte) 50);

        // Act & Assert
        ValidationException result = assertThrows(ValidationException.class, () -> gradeService.assignGrade(assignGradeDto));
        assertEquals("student.notExists", result.getMessageKey());
    }

    @Test
    @DisplayName("Given non-existing subject, when assignGrade called, then throw ValidationException")
    void givenNonExistingSubject_whenAssignGradeCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("Jan", "Kowalski", "A0001");
        GradeCriterion criterion = new GradeCriterion("Egzamin", (byte) 50, student.getId());

        UUID subjectId = UUID.randomUUID();
        AssignGradeDto assignGradeDto = new AssignGradeDto(subjectId, criterion.getId(), student.getId(), (byte) 50);

        when(studentDao.get(assignGradeDto.getStudentId())).thenReturn(Optional.of(student));

        // Act & Assert
        ValidationException result = assertThrows(ValidationException.class, () -> gradeService.assignGrade(assignGradeDto));
        assertEquals("subject.notExists", result.getMessageKey());
    }

    @Test
    @DisplayName("Given non-existing grade criterion, when assignGrade called, then throw ValidationException")
    void givenNonExistingGradeCriterion_whenAssignGradeCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("Jan", "Kowalski", "A0001");
        Subject subject = new Subject("Java");

        UUID gradeCriterionId = UUID.randomUUID();
        AssignGradeDto assignGradeDto = new AssignGradeDto(subject.getId(), gradeCriterionId, student.getId(), (byte) 50);

        when(studentDao.get(assignGradeDto.getStudentId())).thenReturn(Optional.of(student));
        when(subjectDao.get(assignGradeDto.getSubjectId())).thenReturn(Optional.of(subject));

        // Act & Assert
        ValidationException result = assertThrows(ValidationException.class, () -> gradeService.assignGrade(assignGradeDto));
        assertEquals("gradeCriterion.notExists", result.getMessageKey());
    }

    @Test
    @DisplayName("Given grade less than 0, when assignGrade called, then throw ValidationException")
    void givenGradeLessThanZero_whenAssignGradeCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("Jan", "Kowalski", "A0001");
        Subject subject = new Subject("Java");
        GradeCriterion criterion = new GradeCriterion("Egzamin", (byte) 50, subject.getId());
        AssignGradeDto assignGradeDto = new AssignGradeDto(subject.getId(), criterion.getId(), student.getId(), (byte) -1);

        when(studentDao.get(assignGradeDto.getStudentId())).thenReturn(Optional.of(student));
        when(subjectDao.get(assignGradeDto.getSubjectId())).thenReturn(Optional.of(subject));
        when(gradeCriterionDao.get(assignGradeDto.getGradeCriterionId())).thenReturn(Optional.of(criterion));

        // Act & Assert
        ValidationException result = assertThrows(ValidationException.class, () -> gradeService.assignGrade(assignGradeDto));
        assertEquals("grade.wrongGrade", result.getMessageKey());
    }

    @Test
    @DisplayName("Given grade more than maxPoints, when assignGrade called, then throw ValidationException")
    void givenGradeMoreThanMaxPoints_whenAssignGradeCalled_thenThrowValidationException() {
        // Arrange
        Student student = new Student("Jan", "Kowalski", "A0001");
        Subject subject = new Subject("Java");
        GradeCriterion criterion = new GradeCriterion("Egzamin", (byte) 50, subject.getId());
        AssignGradeDto assignGradeDto = new AssignGradeDto(subject.getId(), criterion.getId(), student.getId(), (byte) 60);

        when(studentDao.get(assignGradeDto.getStudentId())).thenReturn(Optional.of(student));
        when(subjectDao.get(assignGradeDto.getSubjectId())).thenReturn(Optional.of(subject));
        when(gradeCriterionDao.get(assignGradeDto.getGradeCriterionId())).thenReturn(Optional.of(criterion));

        // Act & Assert
        ValidationException result = assertThrows(ValidationException.class, () -> gradeService.assignGrade(assignGradeDto));
        assertEquals("grade.wrongGrade", result.getMessageKey());
    }

    @Test
    @DisplayName("Given existing grade and correct dto, when updateGrade called, then update and return updated grade")
    void givenExistingGradeAndCorrectDto_whenUpdateGradeCalled_thenUpdateAndReturnUpdatedGrade() {
        // Arrange
        Grade grade = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50);
        UpdateGradeDto updateGradeDto = new UpdateGradeDto();
        updateGradeDto.setGrade((byte) 60);

        when(gradeDao.get(grade.getId())).thenReturn(Optional.of(grade));

        // Act & Assert
        GradeDto result = assertDoesNotThrow(() -> gradeService.updateGrade(grade.getId(), updateGradeDto));
        assertNotNull(result);
        assertEquals(grade.getId(), result.getId());
        assertEquals(grade.getStudentId(), result.getStudentId());
        assertEquals(grade.getSubjectId(), result.getSubjectId());
        assertEquals((byte) 60, result.getGrade());
        assertEquals(grade.getGradeCriterionId(), result.getGradeCriterionId());
    }

    @Test
    @DisplayName("Given existing grade, when deleteGrade called, then delete and return true")
    void givenExistingGrade_whenDeleteGradeCalled_thenDeleteAndReturnTrue() {
        // Assert
        Grade grade = new Grade(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), (byte) 50);

        when(gradeDao.delete(grade.getId())).thenReturn(true);
        // Act & Assert
        boolean result = assertDoesNotThrow(() -> gradeService.deleteGrade(grade.getId()));
        assertTrue(result);
        verify(gradeDao).delete(grade.getId());
    }
}