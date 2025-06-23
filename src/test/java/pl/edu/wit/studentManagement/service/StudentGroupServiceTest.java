package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.dto.studentGroup.CreateStudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupWithStudentsDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.UpdateStudentGroupDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test suite for {@link StudentGroupService}, verifying methods handling student groups.
 *
 * @author Martin Szum
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentGroupService Test Suite")
class StudentGroupServiceTest {

    @Mock
    private Dao<StudentGroup> studentGroupDao;

    @Mock
    private Dao<Student> studentDao;

    private StudentGroupService studentGroupService;

    @BeforeEach
    void setUp() {
        studentGroupService = new StudentGroupService(studentGroupDao, studentDao);
    }

    @Test
    @DisplayName("Given student groups exist, when getAll is called, then return list of student group DTOs")
    void givenStudentGroupsExist_whenGetAllCalled_thenReturnListOfStudentGroupDto() {
        // Arrange
        StudentGroup studentGroup1 = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        StudentGroup studentGroup2 = new StudentGroup("INF2024", "Informatyka", "Grupa zaoczna");
        List<StudentGroup> studentGroups = List.of(studentGroup1, studentGroup2);

        when(studentGroupDao.getAll()).thenReturn(studentGroups);

        // Act
        List<StudentGroupDto> result = studentGroupService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("INF2023", studentGroup1.getCode());
        assertEquals("INF2024", studentGroup2.getCode());
        assertEquals(studentGroup1.getCode(), result.get(0).getCode());
        assertEquals(studentGroup2.getCode(), result.get(1).getCode());
    }

    @Test
    @DisplayName("Given student group exists, when getWithStudentsById is called, then return studentGroup DTO with students")
    void givenStudentGroupExists_whenGetWithStudentsByIdCalled_thenReturnStudentGroupDtoWithStudents() {
        // Arrange
        StudentGroup studentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        Student student1 = new Student("Jan", "Kowalski", "A0001");
        Student student2 = new Student("Marek", "Nowak", "A0002");
        student1.setStudentGroupId(studentGroup.getId());
        student2.setStudentGroupId(studentGroup.getId());

        List<Student> students = List.of(student1, student2);

        when(studentGroupDao.get(studentGroup.getId())).thenReturn(Optional.of(studentGroup));
        when(studentDao.getAll()).thenReturn(students);

        // Act
        Optional<StudentGroupWithStudentsDto> result = studentGroupService.getWithStudentsById(studentGroup.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("INF2023", result.get().getCode());
        assertEquals("A0001", result.get().getStudents().get(0).getAlbum());
    }

    @Test
    @DisplayName("Given valid CreateStudentGroupDto, when create is called, then save and return new student group")
    void givenValidCreateStudentGroupDto_whenCreateCalled_thenSaveAndReturnNewStudentGroup() throws ValidationException {
        // Arrange
        CreateStudentGroupDto createStudentGroupDto = new CreateStudentGroupDto("INF2023", "Informatyka", "Grupa dzienna");

        // Act
        StudentGroupDto result = studentGroupService.create(createStudentGroupDto);

        // Assert
        assertNotNull(result);
        assertEquals("INF2023", result.getCode());
    }

    @Test
    @DisplayName("Given valid studentGroup id and UpdateStudentGroupDto, when update is called, then update and return studentGroup")
    void givenValidStudentGroupIdAndUpdateStudentGroupDto_whenUpdateCalled_thenUpdateAndReturnStudentGroup() throws ValidationException {
        // Assert
        StudentGroup studentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        UUID id = studentGroup.getId();
        UpdateStudentGroupDto updateStudentGroupDto = new UpdateStudentGroupDto();
        updateStudentGroupDto.setCode("INF2024");

        when(studentGroupDao.get(id)).thenReturn(Optional.of(studentGroup));

        // Act
        StudentGroupDto studentGroupDto = studentGroupService.update(id, updateStudentGroupDto);

        // Assert
        assertNotNull(studentGroupDto);
        assertEquals(id, studentGroupDto.getId());
        assertEquals("INF2024", studentGroupDto.getCode());
    }

    @Test
    @DisplayName("Given existing studentGroup with no students, when delete called, then delete and return true")
    void givenExistingStudentGroupWithNoStudents_whenDeleteCalled_thenDeleteAndReturnTrue() {
        // Assert
        StudentGroup studentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        UUID id = studentGroup.getId();

        when(studentGroupDao.delete(id)).thenReturn(true);

        // Act & Assert
        boolean result = assertDoesNotThrow(() -> studentGroupService.delete(id));
        assertTrue(result);
        verify(studentGroupDao).delete(id);

    }
}