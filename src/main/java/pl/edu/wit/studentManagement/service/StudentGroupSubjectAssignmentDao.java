package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DAO for managing persistence and retrieval of {@link StudentGroupSubjectAssignment} entities.
 * <p>
 * Provides basic CRUD operations and additional query methods to find assignments by student group or subject IDs.
 * Uses a {@link DataStreamHandler} implementation for file-based storage.
 *
 * @author Michał Zawadzki
 */
class StudentGroupSubjectAssignmentDao extends Dao<StudentGroupSubjectAssignment> {
    private final DataStreamHandler<StudentGroupSubjectAssignment> dataHandler;

    /**
     * Constructs the DAO with the specified file path for persistence.
     *
     * @param filePath path to the file used for storing assignments
     */
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
     * Retrieves all assignments associated with the specified student group.
     *
     * @param studentGroupId the ID of the student group
     * @return list of assignments linked to the student group
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
     * Retrieves all assignments associated with the specified subject.
     *
     * @param subjectId the ID of the subject
     * @return list of assignments linked to the subject
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
