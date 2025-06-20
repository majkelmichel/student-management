package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer responsible for managing the associations between student groups and subjects.
 * <p>
 * Provides methods for creating, retrieving, updating, and deleting assignments,
 * as well as querying assignments by student group or subject.
 * Encapsulates business logic and validation before delegating persistence operations to the DAO.
 *
 * @author Michał Zawadzki
 */
class StudentGroupSubjectAssignmentService {
    private final StudentGroupSubjectAssignmentDao dao;

    /**
     * Constructs the service with the specified data file path.
     *
     * @param filePath the path to the file used for assignment persistence
     */
    StudentGroupSubjectAssignmentService(String filePath) {
        this.dao = new StudentGroupSubjectAssignmentDao(filePath);
    }

    /**
     * Creates and persists a new assignment linking a student group with a subject.
     * Validates the assignment before saving.
     *
     * @param studentGroupId the UUID of the student group
     * @param subjectId      the UUID of the subject
     * @throws ValidationException if validation fails due to invalid or missing data
     */
    void createAssignment(UUID studentGroupId, UUID subjectId) throws ValidationException {
        StudentGroupSubjectAssignment assignment = new StudentGroupSubjectAssignment(studentGroupId, subjectId);
        assignment.validate();
        dao.save(assignment);
    }

    /**
     * Retrieves an assignment by its unique identifier.
     *
     * @param id the UUID of the assignment
     * @return an Optional containing the assignment if found, or empty if not found
     */
    Optional<StudentGroupSubjectAssignment> getAssignment(UUID id) {
        return dao.get(id);
    }

    /**
     * Retrieves all existing assignments.
     *
     * @return a list of all student group-subject assignments
     */
    List<StudentGroupSubjectAssignment> getAllAssignments() {
        return dao.getAll();
    }

    /**
     * Updates an existing assignment.
     * Validates the updated data before persisting changes.
     *
     * @param assignment the assignment entity with updated data
     * @throws ValidationException if validation fails
     */
    void updateAssignment(StudentGroupSubjectAssignment assignment) throws ValidationException {
        assignment.validate();
        dao.update(assignment);
    }

    /**
     * Deletes the assignment identified by the given ID.
     *
     * @param id the UUID of the assignment to delete
     * @return {@code true} if the assignment was successfully deleted, {@code false} otherwise
     */
    boolean deleteAssignment(UUID id) {
        return dao.delete(id);
    }

    /**
     * Retrieves all assignments associated with a particular student group.
     *
     * @param studentGroupId the UUID of the student group
     * @return list of assignments linked to the specified student group
     */
    List<StudentGroupSubjectAssignment> getAssignmentsByStudentGroup(UUID studentGroupId) {
        return dao.findByStudentGroupId(studentGroupId);
    }

    /**
     * Retrieves all assignments associated with a particular subject.
     *
     * @param subjectId the UUID of the subject
     * @return list of assignments linked to the specified subject
     */
    List<StudentGroupSubjectAssignment> getAssignmentsBySubject(UUID subjectId) {
        return dao.findBySubjectId(subjectId);
    }
}
