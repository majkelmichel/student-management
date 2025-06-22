package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DAO implementation for {@link GradeCriterion} entities.
 * <p>
 * Provides CRUD operations using a {@link DataStreamHandler}.
 *
 * @author Michał Zawadzki
 */
class GradeCriterionDao extends Dao<GradeCriterion> {
    private final DataStreamHandler<GradeCriterion> dataStreamHandler;

    GradeCriterionDao(DataStreamHandler<GradeCriterion> dataStreamHandler) {
        this.dataStreamHandler = dataStreamHandler;
    }

    @Override
    Optional<GradeCriterion> get(UUID id) {
        try {
            return dataStreamHandler.readAll().stream()
                    .filter(criterion -> criterion.getId() == id)
                    .findFirst();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    List<GradeCriterion> getAll() {
        try {
            return dataStreamHandler.readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    void save(GradeCriterion criterion) throws ValidationException {
        criterion.validate();
        try {
            dataStreamHandler.write(criterion);
        } catch (IOException e) {
        }
    }

    @Override
    void update(GradeCriterion criterion) throws ValidationException {
        criterion.validate();
        try {
            dataStreamHandler.update(criterion);
        } catch (IOException e) {
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
