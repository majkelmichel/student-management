package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.student.CreateStudentDto;
import pl.edu.wit.studentManagement.service.dto.student.StudentDto;
import pl.edu.wit.studentManagement.service.dto.student.UpdateStudentDto;
import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class providing operations for managing students.
 * <p>
 * Supports creating, retrieving, updating, deleting, and searching students.
 * This is the primary public interface for student management functionality.
 * </p>
 */
public class StudentService {
    private final Dao<Student> studentDao;
    private final Dao<StudentGroup> studentGroupDao;

    /**
     * Constructs a StudentService with the specified data access object.
     *
     * @param studentDao the DAO used for student persistence
     * @param studentGroupDao the DAO used for student group persistence
     */
    StudentService(Dao<Student> studentDao, Dao<StudentGroup> studentGroupDao) {
        this.studentDao = studentDao;
        this.studentGroupDao = studentGroupDao;
    }

    /**
     * Retrieves all students.
     *
     * @return a list of all students
     */
    public List<StudentDto> getAllStudents() {
        return studentDao.getAll().stream().map(StudentMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a student by their unique identifier.
     *
     * @param id the unique ID of the student
     * @return an Optional containing the student if found, or empty if not found
     */
    public Optional<StudentDto> getStudentById(UUID id) {
        var student = studentDao.get(id);
        return student.map(StudentMapper::toDto);
    }

    /**
     * Creates a new student.
     *
     * @param createStudentDto the data for the student to create
     * @return the newly created student
     * @throws ValidationException if input data is invalid
     */
    public StudentDto createStudent(CreateStudentDto createStudentDto) throws ValidationException {
        var newStudent = new Student(
                createStudentDto.getFirstName(),
                createStudentDto.getLastName(),
                createStudentDto.getAlbum()
        );

        studentDao.save(newStudent);

        return StudentMapper.toDto(newStudent);
    }

    /**
     * Updates an existing student identified by ID.
     *
     * @param id the ID of the student to update
     * @param updateStudentDto the updated data for the student
     * @return the updated student as a DTO
     * @throws ValidationException if input data is invalid
     * @throws NoSuchElementException if the student does not exist
     */
    public StudentDto updateStudent(UUID id, UpdateStudentDto updateStudentDto) throws ValidationException {
        var student = studentDao.get(id).orElseThrow();

        if (updateStudentDto.getFirstName() != null) student.setFirstName(updateStudentDto.getFirstName());
        if (updateStudentDto.getLastName() != null) student.setLastName(updateStudentDto.getLastName());
        if (updateStudentDto.getAlbum() != null) student.setAlbum(updateStudentDto.getAlbum());

        studentDao.update(student);

        return StudentMapper.toDto(student);
    }

    /**
     * Deletes a student by ID.
     *
     * @param id the ID of the student to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteStudent(UUID id) {
        return studentDao.delete(id);
    }

    /**
     Searches for students matching the specified query.
     * <p>
     * The search checks if the query string (case-insensitive) is contained in any of the following:
     * - Full name (first name + last name)
     * - First name
     * - Last name
     * - Album number
     *
     * @param query the search string
     * @return a list of matching students
     */
    public List<StudentDto> search(String query) {
        var students = studentDao.getAll();
        var lowerQuery = query.toLowerCase();

        return students.stream()
                .filter(s -> {
                    var fullName = s.getFirstName() + " " + s.getLastName();
                    if (fullName.toLowerCase().contains(lowerQuery)) return true;
                    if (s.getFirstName().toLowerCase().contains(lowerQuery)) return true;
                    if (s.getLastName().toLowerCase().contains(lowerQuery)) return true;
                    if (s.getAlbum().toLowerCase().contains(lowerQuery)) return true;
                    return false;
                }).map(StudentMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Assigns a student to a specific group.
     * <p>
     * Updates the student's group membership based on the provided group ID.
     *
     * @param studentId the unique identifier of the student to assign
     * @param groupId   the unique identifier of the group to assign the student to
     * @throws ValidationException if the assignment fails validation
     * @throws NoSuchElementException if the student or group with the given ID does not exist
     */
    public void assignStudentToGroup(UUID studentId, UUID groupId) throws ValidationException {
        var student = studentDao.get(studentId).orElseThrow();
        var studentGroup = studentGroupDao.get(groupId).orElseThrow();

        student.setStudentGroupId(groupId);

        studentDao.update(student);
    }
}
