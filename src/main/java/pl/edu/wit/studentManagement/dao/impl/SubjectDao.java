package pl.edu.wit.studentManagement.dao.impl;

import pl.edu.wit.studentManagement.dao.interfaces.Dao;
import pl.edu.wit.studentManagement.entities.Subject;
import pl.edu.wit.studentManagement.persistence.interfaces.DataStreamHandler;
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
public class SubjectDao implements Dao<Subject> {
    private final DataStreamHandler<Subject> dataStreamHandler;

    public SubjectDao(DataStreamHandler<Subject> dataStreamHandler) {
        this.dataStreamHandler = dataStreamHandler;
    }

    @Override
    public Optional<Subject> get(UUID id) {
        try {
            return dataStreamHandler.readAll().stream()
                    .filter(subject -> subject.getId().equals(id))
                    .findFirst();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Subject> getAll() {
        try {
            return dataStreamHandler.readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public boolean save(Subject subject) throws ValidationException {
        // subject.validate();
        try {
            dataStreamHandler.write(subject);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean update(Subject subject) throws ValidationException {
        // subject.validate();
        try {
            dataStreamHandler.update(subject);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean delete(UUID id) {
        try {
            dataStreamHandler.deleteById(id);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
