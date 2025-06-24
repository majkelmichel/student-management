package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.dto.student.CreateStudentDto;
import pl.edu.wit.studentManagement.service.dto.student.StudentDto;
import pl.edu.wit.studentManagement.service.dto.student.UpdateStudentDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test suite for {@link StudentService}, verifying methods handling students.
 *
 * @author Martin Szum
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Test Suite")
class StudentServiceTest {

    @Mock
    private Dao<Student> studentDao;

    @Mock
    private Dao<StudentGroup> studentGroupDao;

    @Mock
    private Dao<Grade> gradeDao;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentDao, studentGroupDao, gradeDao);
    }

    @Test
    @DisplayName("Given existing students, when getAllStudents is called, then return list of student DTOs")
    void givenExistingStudents_whenGetAllStudentsCalled_thenReturnListOfStudentDto() {
        // Arrange
        Student student1 = new Student("Jan", "Kowalski", "A0001");
        Student student2 = new Student("Maria", "Nowak", "A00002");
        List<Student> students = List.of(student1, student2);

        when(studentDao.getAll()).thenReturn(students);

        // Act
        List<StudentDto> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Jan", result.get(0).getFirstName());
        assertEquals("Maria", result.get(1).getFirstName());
        assertEquals("A0001", result.get(0).getAlbum());
        assertEquals("A00002", result.get(1).getAlbum());
    }

    @Test
    @DisplayName("Given students not assigned to any group, when getStudentsNotAssignedToAnyGroup is called, then return correct student DTOs")
    void givenStudentsNotAssignedToAnyGroup_whenGetStudentsNotAssignedToAnyGroupIsCalled_thenReturnCorrectStudentsDtos() {
        // Arrange
        Student student1 = new Student("Jan", "Kowalski", "A0001");
        Student student2 = new Student("Maria", "Nowak", "A00002");
        List<Student> students = List.of(student1, student2);

        when(studentDao.getAll()).thenReturn(students);

        // Act
        List<StudentDto> result = studentService.getStudentsNotAssignedToAnyGroup();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Jan", result.get(0).getFirstName());
        assertEquals("Maria", result.get(1).getFirstName());
    }

    @Test
    @DisplayName("Given valid createStudentDto, when createStudent is called, then save and return student DTO")
    void givenValidCreateStudentDto_whenCreateStudentCalled_thenSaveAndReturnStudentDto() throws ValidationException {
        // Arrange
        CreateStudentDto createStudentDto = new CreateStudentDto("Jan", "Kowalski", "A0001");

        // Act
        StudentDto result = studentService.createStudent(createStudentDto);

        // Assert
        assertNotNull(result);
        assertEquals("Jan", result.getFirstName());
        assertEquals("Kowalski", result.getLastName());
        assertEquals("A0001", result.getAlbum());
    }

    @Test
    @DisplayName("Given valid student id and updateStudentDto, when updateStudent is called, then update and return student DTO")
    void givenValidStudentIdAndUpdateStudentDto_whenUpdateStudentIsCalled_thenUpdateAndReturnStudentDto() throws ValidationException {
        // Arrange
        Student student1 = new Student("Jan", "Kowalski", "A0001");
        UpdateStudentDto updateStudentDto = new UpdateStudentDto();
        updateStudentDto.setFirstName("Adam");
        UUID student1Id = student1.getId();

        when(studentDao.get(student1Id)).thenReturn(Optional.of(student1));

        // Act
        StudentDto result = studentService.updateStudent(student1Id, updateStudentDto);

        // Assert
        assertNotNull(result);
        assertEquals(student1Id, result.getId());
        assertEquals("Adam", result.getFirstName());
        assertEquals("Kowalski", result.getLastName());
        assertEquals("A0001", result.getAlbum());
    }

    @Test
    @DisplayName("Given valid student id and no grades, when deleteStudent is called, then delete and return true")
    void givenValidStudentIdAndNoGrades_whenDeleteStudentCalled_thenDeleteAndReturnTrue() {
        // Arrange
        Student student1 = new Student("Jan", "Kowalski", "A0001");

        when(studentDao.delete(student1.getId())).thenReturn(true);
        when(gradeDao.getAll()).thenReturn(List.of());

        // Act & Assert
        boolean result = assertDoesNotThrow(() -> studentService.deleteStudent(student1.getId()));
        assertTrue(result);
        verify(studentDao).delete(student1.getId());
    }

    @Test
    @DisplayName("Given valid query, when search is called, then return list of found student DTOs")
    void givenValidQuery_whenSearchCalled_thenReturnListOfFoundStudentDtos() {
        // Arrange
        Student student1 = new Student("Jan", "Kowalski", "A0001");
        Student student2 = new Student("Maria", "Nowak", "A00002");
        List<Student> students = List.of(student1, student2);

        when(studentDao.getAll()).thenReturn(students);

        // Act
        List<StudentDto> oneStudentResult = studentService.search("jan");
        List<StudentDto> twoStudentsResult = studentService.search("wa");

        // Assert
        assertNotNull(oneStudentResult);
        assertEquals(1, oneStudentResult.size());
        assertEquals("Jan", oneStudentResult.get(0).getFirstName());
        assertEquals("Kowalski", oneStudentResult.get(0).getLastName());
        assertEquals("A0001", oneStudentResult.get(0).getAlbum());

        assertNotNull(twoStudentsResult);
        assertEquals(2, twoStudentsResult.size());
        assertEquals("Jan", twoStudentsResult.get(0).getFirstName());
        assertEquals("Maria", twoStudentsResult.get(1).getFirstName());
    }

    @Test
    @DisplayName("Given valid student and group id, when assignStudentToGroup is called, then student is updated")
    void givenValidStudentAndGroupId_whenAssignStudentToGroupCalled_thenStudentIsUpdated() throws ValidationException {
        // Arrange
        Student student1 = new Student("Jan", "Kowalski", "A0001");
        StudentGroup studentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        UUID studentGroupId = studentGroup.getId();
        UUID studentId = student1.getId();

        when(studentDao.get(studentId)).thenReturn(Optional.of(student1));
        when(studentGroupDao.get(studentGroupId)).thenReturn(Optional.of(studentGroup));

        // Act
        studentService.assignStudentToGroup(studentId, studentGroupId);

        // Assert
        assertEquals(studentGroupId, student1.getStudentGroupId());
        verify(studentDao).update(student1);
    }

    @Test
    @DisplayName("Given valid id of student assigned to group, when removeFromGroup is called, then remove assigned group and update")
    void givenValidIdOfStudentAssignedToGroup_whenRemoveFromGroupIsCalled_thenRemoveAssignedGroupAndUpdate() throws ValidationException {
        // Arrange
        Student student1 = new Student("Jan", "Kowalski", "A0001");
        StudentGroup studentGroup = new StudentGroup("INF2023", "Informatyka", "Grupa dzienna");
        UUID studentGroupId = studentGroup.getId();
        UUID studentId = student1.getId();
        student1.setStudentGroupId(studentGroupId);
        when(studentDao.get(studentId)).thenReturn(Optional.of(student1));

        // Act
        studentService.removeFromGroup(studentId);

        // Assert
        assertNull(student1.getStudentGroupId());
        verify(studentDao).update(student1);
    }
}