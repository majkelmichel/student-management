package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.CreateGradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.UpdateGradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.subject.CreateSubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectWithGradeCriteriaDto;
import pl.edu.wit.studentManagement.service.dto.subject.UpdateSubjectDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test suite for {@link SubjectService}, verifying methods handling grades and grade criteria.
 *
 * @author Michał Zawadzki
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("SubjectService Test Suite")
class SubjectServiceTest {

    @Mock
    private Dao<Subject> subjectDao;

    @Mock
    private Dao<GradeCriterion> gradeCriterionDao;

    @Mock
    private Dao<Grade> gradeDao;

    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        subjectService = new SubjectService(subjectDao, gradeCriterionDao, gradeDao);
    }

    @Test
    @DisplayName("Given subjects exist, when all subjects called, then return list of subject DTOs")
    void givenSubjectsExist_whenAllSubjectsCalled_thenReturnListOfSubjectDto() {
        // Arrange
        Subject subject1 = new Subject("Subject 1");
        Subject subject2 = new Subject("Subject 2");
        List<Subject> subjects = List.of(subject1, subject2);

        when(subjectDao.getAll()).thenReturn(subjects);

        // Act
        List<SubjectDto> result = subjectService.getAllSubjects();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Subject 1", result.get(0).getName());
        assertEquals("Subject 2", result.get(1).getName());
        assertEquals(subject1.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Given subject exists, when getSubjectById called, then return subject dto")
    void givenSubjectExists_whenGetSubjectByIdCalled_thenReturnSubjectDto() {
        // Arrange
        Subject subject1 = new Subject("Subject 1");

        when(subjectDao.get(subject1.getId())).thenReturn(Optional.of(subject1));

        UUID subjectId = subject1.getId();

        // Act
        Optional<SubjectDto> result = subjectService.getSubjectById(subjectId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Subject 1", result.get().getName());
        assertEquals(subject1.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Given subject does not exist, when getSubjectById called, then return empty optional")
    void givenSubjectDoesNotExist_whenGetSubjectByIdCalled_thenReturnEmptyOptional() {
        // Arrange
        UUID someId = UUID.randomUUID();

        when(subjectDao.get(someId)).thenReturn(Optional.empty());

        // Act
        Optional<SubjectDto> result = subjectService.getSubjectById(someId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given subject exists with grade criteria, when getSubjectWithGradeCriteriaById called, then return SubjectWithGradeCriteriaDto")
    void givenSubjectExistsWithGradeCriteria_whenGetSubjectWithGradeCriteriaByIdCalled_thenReturnSubjectWithGradeCriteriaDto() {
        // Arrange
        Subject subject1 = new Subject("Subject 1");
        byte maxPoints = 20;
        GradeCriterion criterion = new GradeCriterion("Kolokwium", maxPoints, subject1.getId());

        when(subjectDao.get(subject1.getId())).thenReturn(Optional.of(subject1));
        when(gradeCriterionDao.getAll()).thenReturn(List.of(criterion));

        // Act
        Optional<SubjectWithGradeCriteriaDto> result = subjectService.getSubjectWithGradeCriteriaById(subject1.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Subject 1", result.get().getName());
        assertEquals(1, result.get().getGradeCriteria().size());
        assertEquals("Kolokwium", result.get().getGradeCriteria().get(0).getName());
    }

    @Test
    @DisplayName("Given valid CreateSubjectDto, when createSubject called, then save and return SubjectDto")
    void givenValidCreateSubjectDto_whenCreateSubjectCalled_thenReturnSubjectDto() throws ValidationException {
        // Arrange
        CreateSubjectDto dto = new CreateSubjectDto("Java");

        // Act
        SubjectDto result = subjectService.createSubject(dto);

        // Assert
        assertEquals("Java", result.getName());
        verify(subjectDao).save(any());
    }

    @Test
    @DisplayName("Given existing subject and UpdateSubjectDto, when updateSubject called, then update and return updated SubjectDto")
    void givenExistingSubjectAndUpdateSubjectDto_whenUpdateSubjectCalled_thenReturnSubjectDto() throws ValidationException {
        // Arrange
        Subject subject1 = new Subject("Java");
        UUID subjectId = subject1.getId();

        when(subjectDao.get(subjectId)).thenReturn(Optional.of(subject1));

        UpdateSubjectDto dto = new UpdateSubjectDto();
        dto.setName("C#");

        // Act
        SubjectDto result = subjectService.updateSubject(subjectId, dto);

        // Assert
        assertEquals("C#", result.getName());
        verify(subjectDao).update(any());
    }

    @Test
    @DisplayName("Given non-existing subject, when updateSubject called, then throw NoSuchElementException")
    void givenNonExistingSubject_whenUpdateSubjectCalled_thenThrowNoSuchElementException() {
        // Arrange
        UUID someId = UUID.randomUUID();
        when(subjectDao.get(someId)).thenReturn(Optional.empty());

        UpdateSubjectDto dto = new UpdateSubjectDto();
        dto.setName("Java");

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> subjectService.updateSubject(someId, dto));
    }

    @Test
    @DisplayName("Given existing subject without criteria, when deleteSubject called, then delete and return true")
    void givenExistingSubjectWithoutCriteria_whenDeleteSubjectCalled_thenReturnTrue() {
        // Arrange
        UUID subjectId = UUID.randomUUID();
        when(gradeCriterionDao.getAll()).thenReturn(List.of());
        when(subjectDao.delete(subjectId)).thenReturn(true);

        // Act
        boolean result = assertDoesNotThrow(() -> subjectService.deleteSubject(subjectId));

        // Assert
        assertTrue(result);
        verify(subjectDao).delete(subjectId);
    }

    @Test
    @DisplayName("Given a subject with criteria, when deleteSubject called, then throw ValidationException")
    void givenExistingSubjectWithCriteria_whenDeleteSubjectCalled_thenThrowValidationException() {
        // Arrange
        Subject subject1 = new Subject("Java");
        byte maxPoints = 20;
        GradeCriterion gradeCriterion = new GradeCriterion("Kolokwium", maxPoints, subject1.getId());
        when(gradeCriterionDao.getAll()).thenReturn(List.of(gradeCriterion));

        // Act & Assert
        ValidationException ex = assertThrows(ValidationException.class, () -> subjectService.deleteSubject(subject1.getId()));
        assertEquals("subject.delete.hasGradeCriteria", ex.getMessageKey());
        verify(subjectDao, never()).delete(any());
    }

    @Test
    @DisplayName("Given correct CreateGradeCriterionDto, when addCriterionToSubject called, then assign criterion and return correct DTO")
    void givenCorrectCreateGradeCriterionDto_whenCreateGradeCriterionCalled_thenAssignCorrectDTO() throws ValidationException {
        // Arrange
        Subject subject1 = new Subject("Java");
        byte maxPoints = 30;

        CreateGradeCriterionDto dto = new CreateGradeCriterionDto("Test 1", maxPoints);

        when(subjectDao.get(subject1.getId())).thenReturn(Optional.of(subject1));

        // Act
        GradeCriterionDto result = assertDoesNotThrow(() -> subjectService.addCriterionToSubject(subject1.getId(), dto));

        // Assert
        assertEquals("Test 1", result.getName());
        assertEquals(maxPoints, result.getMaxPoints());
        verify(gradeCriterionDao).save(any());
    }

    @Test
    @DisplayName("Given non-existing subject, when addCriterionToSubject called, then throw ValidationException")
    void givenNonExistingSubject_whenCreateGradeCriterionCalled_thenThrowValidationException() {
        // Arrange
        UUID subjectId = UUID.randomUUID();

        CreateGradeCriterionDto dto = new CreateGradeCriterionDto("Praca domowa 1", (byte) 5);

        // Act & Assert
        assertThrows(ValidationException.class, () -> subjectService.addCriterionToSubject(subjectId, dto));
    }

    @Test
    @DisplayName("Given existing grade criterion with no grades, when deleteGradeCriterion called, then delete and return true")
    void givenExistingGradeCriterion_whenDeleteGradeCriterionCalled_thenDeleteAndReturnTrue() {
        // Assert
        Subject subject = new Subject("Java");
        GradeCriterion gradeCriterion = new GradeCriterion("Kolos 1", (byte) 20, subject.getId());

        when(gradeDao.getAll()).thenReturn(List.of());
        when(gradeCriterionDao.delete(gradeCriterion.getId())).thenReturn(true);

        // Act
        boolean result = assertDoesNotThrow(() -> subjectService.deleteGradeCriterion(gradeCriterion.getId()));

        // Assert
        assertTrue(result);
        verify(gradeCriterionDao).delete(gradeCriterion.getId());
    }

    @Test
    @DisplayName("Given existing grade criterion and correct dto, when updateGradeCriterion called, then update and return updated criterion")
    void givenExistingGradeCriterion_whenUpdateGradeCriterionCalled_thenUpdateAndReturnUpdatedCriterion() throws ValidationException {
        // Arrange
        Subject subject = new Subject("C++");
        GradeCriterion gradeCriterion = new GradeCriterion("Test 1", (byte) 20, subject.getId());

        UpdateGradeCriterionDto dto = new UpdateGradeCriterionDto("Kolokwium 2", null);

        when(gradeCriterionDao.get(gradeCriterion.getId())).thenReturn(Optional.of(gradeCriterion));

        // Act
        GradeCriterionDto result = assertDoesNotThrow(() -> subjectService.updateGradeCriterion(gradeCriterion.getId(), dto));

        // Assert
        assertEquals("Kolokwium 2", result.getName());
        assertEquals((byte) 20, result.getMaxPoints());
        verify(gradeCriterionDao).update(gradeCriterion);
    }
}