package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data Access Object (DAO) implementation for {@link Subject} entities.
 * <p>
 * Provides CRUD operations using a {@link DataStreamHandler} for persistence.
 * </p>
 *
 * @author Michał Zawadzki
 */
class SubjectDao extends Dao<Subject> {
    private final DataStreamHandler<Subject> dataStreamHandler;

    SubjectDao(DataStreamHandler<Subject> dataStreamHandler) {
        this.dataStreamHandler = dataStreamHandler;
    }

    @Override
    Optional<Subject> get(UUID id) {
        try {
            return dataStreamHandler.readAll().stream()
                    .filter(subject -> subject.getId().equals(id))
                    .findFirst();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    List<Subject> getAll() {
        try {
            return dataStreamHandler.readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    boolean save(Subject subject) throws ValidationException {
        // subject.validate();
        try {
            dataStreamHandler.write(subject);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    boolean update(Subject subject) throws ValidationException {
        // subject.validate();
        try {
            dataStreamHandler.update(subject);
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
