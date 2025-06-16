package pl.edu.wit.studentManagement.dao.impl;

import pl.edu.wit.studentManagement.dao.interfaces.Dao;
import pl.edu.wit.studentManagement.persistence.interfaces.DataStreamHandler;
import pl.edu.wit.studentManagement.entities.Student;
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
public class StudentDao implements Dao<Student> {
    private final DataStreamHandler<Student> dataStreamHandler;

    public StudentDao(DataStreamHandler<Student> dataStreamHandler) {
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
    public Optional<Student> get(UUID id) {
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
    public List<Student> getAll() {
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
    public boolean save(Student student) throws ValidationException {
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
    public boolean update(Student student) throws ValidationException {
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
    public boolean delete(UUID id) {
        try {
            dataStreamHandler.deleteById(id);
            return true;
        } catch (IOException e) {
            // TODO: Handle Exception
            return false;
        }
    }
}