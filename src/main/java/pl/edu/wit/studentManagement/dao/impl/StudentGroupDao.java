package pl.edu.wit.studentManagement.dao.impl;

import pl.edu.wit.studentManagement.dao.interfaces.Dao;
import pl.edu.wit.studentManagement.entities.StudentGroup;
import pl.edu.wit.studentManagement.persistence.interfaces.DataStreamHandler;
import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data Access Object (DAO) implementation for {@link StudentGroup} entities.
 * <p>
 * This class provides methods to perform CRUD operations on student group data,
 * delegating the actual read/write logic to a {@link DataStreamHandler}.
 * </p>
 *
 * @see StudentGroup
 * @see DataStreamHandler
 * @see Dao
 * @see ValidationException
 *
 * @author  Michał Zawadzki
 */
public class StudentGroupDao implements Dao<StudentGroup> {
    private final DataStreamHandler<StudentGroup> dataStreamHandler;

    /**
     * Constructs a new {@code StudentGroupDao} with the given data stream handler.
     *
     * @param dataStreamHandler the handler responsible for reading/writing {@code StudentGroup} data
     */
    public StudentGroupDao(DataStreamHandler<StudentGroup> dataStreamHandler) {
        this.dataStreamHandler = dataStreamHandler;
    }

    /**
     * Retrieves a {@link StudentGroup} by its unique identifier.
     *
     * @param id the unique ID of the student group to retrieve
     * @return an {@link Optional} containing the {@code StudentGroup} if found,
     *         or an empty {@code Optional} if no group exists with the given ID
     */
    @Override
    public Optional<StudentGroup> get(UUID id) {
        try {
            return dataStreamHandler.readAll().stream()
                    .filter(g -> g.getId().equals(id))
                    .findFirst();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all student group records.
     *
     * @return a {@link List} containing all {@link StudentGroup} entries,
     *         or an empty list if an I/O error occurs
     */
    @Override
    public List<StudentGroup> getAll() {
        try {
            return dataStreamHandler.readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    /**
     * Saves a new {@link StudentGroup} record.
     *
     * @param studentGroup the {@link StudentGroup} to be saved
     * @return {@code true} if the operation succeeds, {@code false} otherwise
     * @throws ValidationException if the student group data is invalid
     */
    @Override
    public boolean save(StudentGroup studentGroup) throws ValidationException {
        studentGroup.validate();
        try {
            dataStreamHandler.write(studentGroup);
            return true;
        } catch (IOException e) {
            // TODO: Handle exception
            return false;
        }
    }

    /**
     * Updates an existing {@link StudentGroup} record.
     *
     * @param studentGroup the {@link StudentGroup} containing updated information
     * @return {@code true} if the update succeeds, {@code false} otherwise
     * @throws ValidationException if the student group data is invalid
     */
    @Override
    public boolean update(StudentGroup studentGroup) throws ValidationException {
        studentGroup.validate();
        try {
            dataStreamHandler.update(studentGroup);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Deletes a {@link StudentGroup} by its unique identifier.
     *
     * @param id the ID of the student group to delete
     * @return {@code true} if the deletion succeeds, {@code false} otherwise
     */
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
