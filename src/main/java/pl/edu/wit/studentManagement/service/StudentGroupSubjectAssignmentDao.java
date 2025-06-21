package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.DataAccessException;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

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
            return Optional.empty();
        }
    }

    @Override
    List<StudentGroupSubjectAssignment> getAll() {
        try {
            return dataHandler.readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    void save(StudentGroupSubjectAssignment assignment) throws ValidationException {
        assignment.validate();
        try {
            dataHandler.write(assignment);
        } catch (IOException e) {
            throw new DataAccessException("studentGroupSubjectAssignment.save.failed", e);
        }
    }

    @Override
    void update(StudentGroupSubjectAssignment assignment) throws ValidationException {
        assignment.validate();
        try {
            dataHandler.update(assignment);
        } catch (IOException e) {
            throw new DataAccessException("studentGroupSubjectAssignment.update.failed", e);
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

}
