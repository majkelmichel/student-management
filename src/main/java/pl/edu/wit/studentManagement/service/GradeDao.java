package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.DataAccessException;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data Access Object (DAO) for managing {@link Grade} entities.
 * <p>
 * This class provides CRUD operations backed by a {@link DataStreamHandler}
 * for persistence using Java serialization.
 * <p>
 * Before persisting or updating entities, the {@link Grade#validate()} method
 * is invoked to ensure the entity is in a consistent state.
 * <p>
 * Methods catch I/O exceptions internally and degrade gracefully, returning empty
 * collections or optionals when applicable.
 *
 * @see Grade
 * @see Dao
 * @see DataStreamHandler
 * @see ValidationException
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
    void save(Grade grade) throws ValidationException {
        grade.validate();
        try {
            dataStreamHandler.write(grade);
        } catch (IOException e) {
            throw new DataAccessException("grade.save.failed", e);
        }
    }

    @Override
    void update(Grade grade) throws ValidationException {
        grade.validate();
        try {
            dataStreamHandler.update(grade);
        } catch (IOException e) {
            throw new DataAccessException("grade.update.failed", e);
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
