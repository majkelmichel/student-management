package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data Access Object (DAO) implementation for {@link Student} entities.
 * <p>
 * This class provides methods to perform CRUD operations on student data,
 * delegating the actual read/write logic to a {@link DataStreamHandler}.
 * </p>
 *
 * @see Student
 * @see DataStreamHandler
 * @see Dao
 * @see ValidationException
 *
 * @author Michał Zawadzki
 */
class StudentDao extends Dao<Student> {
    private final DataStreamHandler<Student> dataStreamHandler;

    StudentDao(DataStreamHandler<Student> dataStreamHandler) {
        this.dataStreamHandler = dataStreamHandler;
    }

    /**
     * Retrieves s {@link Student} by their unique identifier
     *
     * @param id the unique ID of the student to retrieve
     * @return an {@link Optional} containing the {@code Student} if found,
     *          or an empty {@code Optional} if not student exists with the given ID
     */
    @Override
    Optional<Student> get(UUID id) {
        try {
            return dataStreamHandler.readAll().stream()
                    .filter(student -> student.getId().equals(id))
                    .findFirst();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all student records.
     *
     * @return a {@link List} containing all {@link Student} entries,
     *         or an empty list if an I/O error occurs
     */
    @Override
    List<Student> getAll() {
        try {
            return dataStreamHandler.readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    /**
     * Saves a new {@link Student} record.
     *
     * @param student the {@link Student} to be saved
     * @throws ValidationException if the student data is invalid
     */
    @Override
    boolean save(Student student) throws ValidationException {
        student.validate();
        try {
            dataStreamHandler.write(student);
            return true;
        } catch (IOException e) {
            // TODO: Handle exception
            return false;
        }
    }

    /**
     * Updates an existing {@link Student} record.
     *
     * @param student the {@link Student} containing updated information
     * @throws ValidationException if the student data is invalid
     */
    @Override
    boolean update(Student student) throws ValidationException {
        student.validate();
        try {
            dataStreamHandler.update(student);
            return true;
        } catch (IOException e) {
            // TODO: Handle Exception
            return false;
        }
    }

    /**
     * Deletes a {@link Student} by their unique identifier.
     *
     * @param id the ID of the student to delete
     */
    @Override
    boolean delete(UUID id) {
        try {
            dataStreamHandler.deleteById(id);
            return true;
        } catch (IOException e) {
            // TODO: Handle Exception
            return false;
        }
    }
}