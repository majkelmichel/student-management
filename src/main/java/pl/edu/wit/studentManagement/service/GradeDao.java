package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DAO implementation for {@link Grade} entities.
 * <p>
 * Uses a {@link DataStreamHandler} for persistence of student grades.
 * </p>
 *
 * @author Michał Zawadzki
 */
class GradeDao extends Dao<Grade> {
    private final DataStreamHandler<Grade> dataStreamHandler;

    GradeDao(DataStreamHandler<Grade> dataStreamHandler) {
        this.dataStreamHandler = dataStreamHandler;
    }

    @Override
    Optional<Grade> get(UUID id) {
        try {
            return dataStreamHandler.readAll().stream()
                    .filter(grade -> grade.getStudentId().equals(id)) // May need revision if ID uniqueness differs
                    .findFirst();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    List<Grade> getAll() {
        try {
            return dataStreamHandler.readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    boolean save(Grade grade) throws ValidationException {
        // grade.validate();
        try {
            dataStreamHandler.write(grade);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    boolean update(Grade grade) throws ValidationException {
        // grade.validate();
        try {
            dataStreamHandler.update(grade);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    boolean delete(UUID id) {
        try {
            dataStreamHandler.deleteById(id);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
