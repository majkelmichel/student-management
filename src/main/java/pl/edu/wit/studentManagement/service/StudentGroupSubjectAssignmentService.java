package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for managing assignments between student groups and subjects.
 * Provides basic CRUD operations and retrieval by student group or subject.
 *
 * @author Michał Zawadzki
 */
class StudentGroupSubjectAssignmentService {
    private final StudentGroupSubjectAssignmentDao dao;

    StudentGroupSubjectAssignmentService(String filePath) {
        this.dao = new StudentGroupSubjectAssignmentDao(filePath);
    }

    /**
     * Creates and saves a new assignment between a student group and a subject.
     *
     * @param studentGroupId the student group ID
     * @param subjectId      the subject ID
     * @throws ValidationException if assignment data is invalid
     */
    void createAssignment(UUID studentGroupId, UUID subjectId) throws ValidationException {
        StudentGroupSubjectAssignment assignment = new StudentGroupSubjectAssignment(studentGroupId, subjectId);
        assignment.validate();
        dao.save(assignment);
    }

    /**
     * Retrieves an assignment by its ID.
     *
     * @param id the assignment ID
     * @return the assignment if found, otherwise empty
     */
    Optional<StudentGroupSubjectAssignment> getAssignment(UUID id) {
        return dao.get(id);
    }

    /**
     * Retrieves all assignments.
     *
     * @return list of all assignments
     */
    List<StudentGroupSubjectAssignment> getAllAssignments() {
        return dao.getAll();
    }

    /**
     * Updates an existing assignment.
     *
     * @param assignment the updated assignment
     * @throws ValidationException if data is invalid
     */
    void updateAssignment(StudentGroupSubjectAssignment assignment) throws ValidationException {
        assignment.validate();
        dao.update(assignment);
    }

    /**
     * Deletes an assignment by its ID.
     *
     * @param id the assignment ID
     * @return true if deleted, false otherwise
     */
    boolean deleteAssignment(UUID id) {
        return dao.delete(id);
    }

    /**
     * Retrieves all assignments for a specific student group.
     *
     * @param studentGroupId the student group ID
     * @return list of assignments
     */
    List<StudentGroupSubjectAssignment> getAssignmentsByStudentGroup(UUID studentGroupId) {
        return dao.findByStudentGroupId(studentGroupId);
    }

    /**
     * Retrieves all assignments for a specific subject.
     *
     * @param subjectId the subject ID
     * @return list of assignments
     */
    List<StudentGroupSubjectAssignment> getAssignmentsBySubject(UUID subjectId) {
        return dao.findBySubjectId(subjectId);
    }
}