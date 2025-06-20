package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DAO for managing {@link StudentGroupSubjectAssignment} persistence.
 * Supports CRUD operations.
 *
 * @author Michał Zawadzki
 */
class StudentGroupSubjectAssignmentDao extends Dao<StudentGroupSubjectAssignment> {
    private final DataStreamHandler<StudentGroupSubjectAssignment> dataHandler;

    StudentGroupSubjectAssignmentDao(String filePath) {
        this.dataHandler = new StudentGroupSubjectAssignmentDataStreamHandler(filePath);
    }

    @Override
    Optional<StudentGroupSubjectAssignment> get(UUID id) {
        try {
            return dataHandler.readAll().stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read assignments", e);
        }
    }

    @Override
    List<StudentGroupSubjectAssignment> getAll() {
        try {
            return dataHandler.readAll();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read assignments", e);
        }
    }

    @Override
    void save(StudentGroupSubjectAssignment assignment) throws ValidationException {
        assignment.validate();
        try {
            dataHandler.write(assignment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save assignment", e);
        }
    }

    @Override
    void update(StudentGroupSubjectAssignment assignment) throws ValidationException {
        assignment.validate();
        try {
            dataHandler.update(assignment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update assignment", e);
        }
    }

    @Override
    boolean delete(UUID id) {
        try {
            dataHandler.deleteById(id);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Finds all assignments by StudentGroup ID.
     *
     * @param studentGroupId the student group ID
     * @return list of assignments for the given student group
     */
    List<StudentGroupSubjectAssignment> findByStudentGroupId(UUID studentGroupId) {
        try {
            return dataHandler.readAll().stream()
                    .filter(a -> a.getStudentGroupId().equals(studentGroupId))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read assignments", e);
        }
    }

    /**
     * Finds all assignments by Subject ID.
     *
     * @param subjectId the subject ID
     * @return list of assignments for the given subject
     */
    List<StudentGroupSubjectAssignment> findBySubjectId(UUID subjectId) {
        try {
            return dataHandler.readAll().stream()
                    .filter(a -> a.getSubjectId().equals(subjectId))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read assignments", e);
        }
    }
}
