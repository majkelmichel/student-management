package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link Subject} for the {@link Subject} entity.
 * <p>
 * This class manages the persistence of {@link Subject} objects by serializing
 * and deserializing a list of them to a file.
 * <p>
 * It supports CRUD operations by loading all objects into memory, modifying the list,
 * and writing it back to ensure data consistency.
 * <p>
 * The file is created if it does not exist upon instantiation and initialized with an empty list.
 * <p>
 * Usage example:
 * <pre>{@code
 * DataStreamHandler<Subject> handler = new SubjectDataStreamHandler("subjects.dat");
 * handler.write(new Subject(...));
 * List<Subject> all = handler.readAll();
 * }</pre>
 *
 * @author Michał Zawadzki
 */
class SubjectDataStreamHandler extends DataStreamHandler<Subject> {

    /**
     * Constructs a new SubjectDataStreamHandler with the specified file path.
     *
     * @param filePath the path to the file where grade data will be stored
     */
    SubjectDataStreamHandler(String filePath) {
        super(filePath);
    }

    /**
     * Reads a Subject object from the input stream.
     *
     * @param in the input stream to read from
     * @return the Subject object read from the stream, or null if end of file is reached
     * @throws IOException if an I/O error occurs while reading from the stream
     */
    @Override
    Subject readObject(DataInputStream in) throws IOException {
        try {
            UUID id = readUuid(in);
            String name = in.readUTF();

            return new Subject(id, name);
        } catch (EOFException e) {
            return null;
        }
    }

    /**
     * Writes a Subject object to the output stream.
     *
     * @param out   the output stream to write to
     * @param subject the Subject object to write
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    @Override
    void writeObject(DataOutputStream out, Subject subject) throws IOException {
        writeUuid(subject.getId(), out);
        out.writeUTF(subject.getName());
    }
}
