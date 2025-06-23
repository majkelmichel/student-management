package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment.CreateStudentGroupSubjectAssignmentDto;
import pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment.StudentGroupSubjectAssignmentDto;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *  Unit test suite for {@link StudentGroupSubjectAssignmentService}, verifying methods handling studentGroupSubjectAssignments.
 *
 * @author Martin Szum
 */

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("StudentGroupSubjectAssignmentService Test Suite")
class StudentGroupSubjectAssignmentServiceTest {

    @Mock
    private Dao<StudentGroupSubjectAssignment> studentGroupSubjectAssignmentDao;

    private StudentGroupSubjectAssignmentService studentGroupSubjectAssignmentService;

    @BeforeEach
    void setUp() {
        studentGroupSubjectAssignmentService = new StudentGroupSubjectAssignmentService(studentGroupSubjectAssignmentDao);
    }

    @Test
    @DisplayName("Given correct CreateStudentGroupSubjectAssignmentDto, when createAssignment called, then save")
    void givenCorrectCreateStudentGroupSubjectAssignmentDto_whenCreateAssigmentCalled_thenSave() {
        // Arrange
        StudentGroup studentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        Subject subject = new Subject("Grafy i sieci");
        UUID studentGroupId = studentGroup.getId();
        UUID subjectId = subject.getId();
        CreateStudentGroupSubjectAssignmentDto createStudentGroupSubjectAssignmentDto = new CreateStudentGroupSubjectAssignmentDto(studentGroupId, subjectId);

        // Act & Assert
        assertDoesNotThrow(() -> studentGroupSubjectAssignmentService.createAssignment(createStudentGroupSubjectAssignmentDto));
    }

    @Test
    @DisplayName("Given existing studentGroup, when getAssignmentByStudentGroup is called, then return list of assignments DTOs")
    void givenExistingStudentGroup_whenGetAssignmentByStudentGroupCalled_thenReturnListOfAssignments() {
        // Arrange
        StudentGroup studentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        Subject subject = new Subject("Grafy i sieci");
        UUID studentGroupId = studentGroup.getId();
        UUID subjectId = subject.getId();
        StudentGroupSubjectAssignment studentGroupSubjectAssignment = new StudentGroupSubjectAssignment(
                studentGroupId, subjectId);
        when(studentGroupSubjectAssignmentDao.getAll()).thenReturn(List.of(studentGroupSubjectAssignment));

        // Act
        List<StudentGroupSubjectAssignmentDto> result = studentGroupSubjectAssignmentService.getAssignmentsByStudentGroup(studentGroupId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(studentGroupId, result.get(0).getStudentGroupId());
        assertEquals(subjectId, result.get(0).getSubjectId());
    }

    @Test
    @DisplayName("Given valid studentGroupSubjectAssignment, when deleteAssignment is called, then delete and return true")
    void givenExistingStudentGroupSubjectAssignment_whenDeleteAssignmentCalled_thenDeleteAndReturnTrue() {
        // Assert
        StudentGroup studentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        UUID studentGroupId = studentGroup.getId();
        StudentGroupSubjectAssignment studentGroupSubjectAssignment = new StudentGroupSubjectAssignment(
                studentGroupId, UUID.randomUUID());
        when(studentGroupSubjectAssignmentDao.delete(studentGroupSubjectAssignment.getId())).thenReturn(true);
        // Act & Assert
        boolean result = assertDoesNotThrow(() -> studentGroupSubjectAssignmentService.deleteAssignment(studentGroupSubjectAssignment.getId()));
        assertTrue(result);
        verify(studentGroupSubjectAssignmentDao).delete(studentGroupSubjectAssignment.getId());
    }
}