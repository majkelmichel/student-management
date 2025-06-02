package pl.edu.wit.studentManagement.dao.impl;

import pl.edu.wit.studentManagement.dao.interfaces.Dao;
import pl.edu.wit.studentManagement.entities.GradeCriterion;
import pl.edu.wit.studentManagement.persistence.interfaces.DataStreamHandler;
import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DAO implementation for {@link GradeCriterion} entities.
 * <p>
 * Provides CRUD operations using a {@link DataStreamHandler}.
 * </p>
 *
 * @author Michał Zawadzki
 */
public class GradeCriterionDao implements Dao<GradeCriterion> {
    private final DataStreamHandler<GradeCriterion> dataStreamHandler;

    public GradeCriterionDao(DataStreamHandler<GradeCriterion> dataStreamHandler) {
        this.dataStreamHandler = dataStreamHandler;
    }

    @Override
    public Optional<GradeCriterion> get(UUID id) {
        try {
            return dataStreamHandler.readAll().stream()
                    .filter(criterion -> criterion.getId() == id)
                    .findFirst();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GradeCriterion> getAll() {
        try {
            return dataStreamHandler.readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public boolean save(GradeCriterion criterion) throws ValidationException {
        // criterion.validate();
        try {
            dataStreamHandler.write(criterion);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean update(GradeCriterion criterion) throws ValidationException {
        // criterion.validate();
        try {
            dataStreamHandler.update(criterion);
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
