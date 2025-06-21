package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment.CreateStudentGroupSubjectAssignmentDto;
import pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment.StudentGroupSubjectAssignmentDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service layer responsible for managing the associations between student groups and subjects.
 * <p>
 * Provides methods for creating, retrieving, and deleting assignments,
 * as well as querying assignments by student group or subject.
 * Encapsulates business logic and validation before delegating persistence operations to the DAO.
 *
 * @author Michał Zawadzki
 */
public class StudentGroupSubjectAssignmentService {
    private final Dao<StudentGroupSubjectAssignment> dao;

    /**
     * Constructs a StudentService with the specified data access object.
     *
     * @param dao the DAO used for student group to subject assignment persistence
     */
    StudentGroupSubjectAssignmentService(Dao<StudentGroupSubjectAssignment> dao) {
        this.dao = dao;
    }

    /**
     * Creates and persists a new assignment linking a student group with a subject.
     * Validates the assignment before saving.
     *
     * @param dto DTO containing UUIDs of student group and subject used to create a new assignment
     * @throws ValidationException if validation fails due to invalid or missing data
     */
    public void createAssignment(CreateStudentGroupSubjectAssignmentDto dto) throws ValidationException {
        StudentGroupSubjectAssignment assignment = new StudentGroupSubjectAssignment(dto.getStudentGroupId(), dto.getSubjectId());
        assignment.validate();
        dao.save(assignment);
    }

    /**
     * Retrieves an assignment by its unique identifier.
     *
     * @param id the UUID of the assignment
     * @return an Optional containing the assignment if found, or empty if not found
     */
    public Optional<StudentGroupSubjectAssignmentDto> getAssignment(UUID id) {
        return dao.get(id).map(StudentGroupSubjectAssignmentMapper::toDto);
    }

    /**
     * Retrieves all existing assignments.
     *
     * @return a list of all student group-subject assignments
     */
    public List<StudentGroupSubjectAssignmentDto> getAllAssignments() {
        return dao.getAll()
                .stream()
                .map(StudentGroupSubjectAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes the assignment identified by the given ID.
     *
     * @param id the UUID of the assignment to delete
     * @return {@code true} if the assignment was successfully deleted, {@code false} otherwise
     */
    public boolean deleteAssignment(UUID id) {
        return dao.delete(id);
    }

    /**
     * Retrieves all assignments associated with a particular student group.
     *
     * @param studentGroupId the UUID of the student group
     * @return list of assignments linked to the specified student group
     */
    public List<StudentGroupSubjectAssignmentDto> getAssignmentsByStudentGroup(UUID studentGroupId) {
        return dao.getAll()
                .stream()
                .filter(a -> a.getStudentGroupId().equals(studentGroupId))
                .map(StudentGroupSubjectAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all assignments associated with a particular subject.
     *
     * @param subjectId the UUID of the subject
     * @return list of assignments linked to the specified subject
     */
    public List<StudentGroupSubjectAssignmentDto> getAssignmentsBySubject(UUID subjectId) {
        return dao.getAll()
                .stream()
                .filter(a -> a.getSubjectId().equals(subjectId))
                .map(StudentGroupSubjectAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
