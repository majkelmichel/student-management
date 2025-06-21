package pl.edu.wit.studentManagement.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Abstract handler for reading and writing data streams of objects of type T.
 * Provides methods for CRUD operations on persistent storage using streams.
 *
 * @param <T> the type of object this handler processes
 *
 * @author Michał Zawadzki
 */
abstract class DataStreamHandler<T> {

    /**
     * Reads all objects of type T from the data stream.
     *
     * @return a list of all objects read
     * @throws IOException if an I/O error occurs during reading
     */
    abstract List<T> readAll() throws IOException;

    /**
     * Writes a new object to the data stream.
     *
     * @param object the object to write
     * @throws IOException if an I/O error occurs during writing
     */
    abstract void write(T object) throws IOException;

    /**
     * Updates an existing object in the data stream.
     *
     * @param object the object with updated data
     * @throws IOException if an I/O error occurs during updating
     */
    abstract void update(T object) throws IOException;

    /**
     * Deletes an object identified by its UUID from the data stream.
     *
     * @param id the UUID of the object to delete
     * @throws IOException if an I/O error occurs during deletion
     */
    abstract void deleteById(UUID id) throws IOException;
}
