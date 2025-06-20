package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.studentGroup.CreateStudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupWithStudentsDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.UpdateStudentGroupDto;
import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing {@link StudentGroup} entities.
 * <p>
 * Provides methods to create, update, delete, and retrieve student groups,
 * including fetching groups with their associated students.
 * <p>
 * Enforces validation such as preventing deletion of groups that contain students.
 *
 * @author Michał Zawadzki
 */
public class StudentGroupService {
    private final Dao<StudentGroup> studentGroupDao;
    private final Dao<Student> studentDao;

    /**
     * Constructs a StudentGroupService with DAOs for student groups and students.
     *
     * @param studentGroupDao DAO for managing student groups
     * @param studentDao DAO for managing students
     */
    StudentGroupService(Dao<StudentGroup> studentGroupDao, Dao<Student> studentDao) {
        this.studentGroupDao = studentGroupDao;
        this.studentDao = studentDao;
    }

    /**
     * Retrieves all student groups as DTOs.
     *
     * @return list of all student groups
     */
    public List<StudentGroupDto> getAll() {
        return studentGroupDao.getAll().stream()
                .map(StudentGroupMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a student group by ID along with its associated students.
     *
     * @param id the UUID of the student group
     * @return optional containing the student group with students if found
     */
    public Optional<StudentGroupWithStudentsDto> getWithStudentsById(UUID id) {
        var studentGroup = studentGroupDao.get(id);
        var students = studentDao.getAll()
                .stream()
                .filter(student -> student.getStudentGroupId().equals(id))
                .collect(Collectors.toList());

        return studentGroup.map(g -> StudentGroupMapper.toWithStudentsDto(g, students));
    }

    /**
     * Creates a new student group from the provided DTO.
     *
     * @param studentGroupDto DTO containing student group creation data
     * @return the created student group as a DTO
     * @throws ValidationException if validation fails during creation
     */
    public StudentGroupDto create(CreateStudentGroupDto studentGroupDto) throws ValidationException {
        var newGroup = new StudentGroup(
                studentGroupDto.getCode(),
                studentGroupDto.getSpecialization(),
                studentGroupDto.getDescription()
        );

        studentGroupDao.save(newGroup);

        return StudentGroupMapper.toDto(newGroup);
    }

    /**
     * Updates an existing student group identified by ID with new data from the DTO.
     *
     * @param id  the UUID of the student group to update
     * @param dto DTO containing updated fields for the student group
     * @return the updated student group as a DTO
     * @throws ValidationException if validation fails or group not found
     */
    public StudentGroupDto update(UUID id, UpdateStudentGroupDto dto) throws ValidationException {
        var studentGroup = studentGroupDao.get(id).orElseThrow();

        if (dto.getCode() != null) studentGroup.setCode(dto.getCode());
        if (dto.getDescription() != null) studentGroup.setDescription(dto.getDescription());
        if (dto.getSpecialization() != null) studentGroup.setSpecialization(dto.getSpecialization());

        studentGroupDao.update(studentGroup);

        return StudentGroupMapper.toDto(studentGroup);
    }

    /**
     * Deletes the student group with the given ID if it contains no students.
     *
     * @param id the UUID of the student group to delete
     * @return true if deletion was successful
     * @throws ValidationException if the student group contains students and cannot be deleted
     */
    public boolean delete(UUID id) throws ValidationException {
        var studentsInGroup = studentDao.getAll().stream()
                .filter(student -> student.getStudentGroupId().equals(id))
                .collect(Collectors.toList());

        if (!studentsInGroup.isEmpty()) throw new ValidationException("studentGroup.delete.notEmpty");

        return studentGroupDao.delete(id);
    }
}
