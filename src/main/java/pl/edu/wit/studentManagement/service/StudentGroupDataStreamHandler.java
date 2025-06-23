package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.UUID;

/**
 * Implementation of {@link StudentGroup} for the {@link StudentGroup} entity.
 * <p>
 * This class manages the persistence of {@link StudentGroup} objects by serializing
 * and deserializing a list of them to a file.
 * <p>
 * It supports CRUD operations by loading all objects into memory, modifying the list,
 * and writing it back to ensure data consistency.
 * <p>
 * The file is created if it does not exist upon instantiation and initialized with an empty list.
 * <p>
 * Usage example:
 * <pre>{@code
 * DataStreamHandler<StudentGroup> handler = new StudentGroupDataStreamHandler("groups.dat");
 * handler.write(new StudentGroup(...));
 * List<StudentGroup> all = handler.readAll();
 * }</pre>
 *
 * @author Michał Zawadzki
 */
class StudentGroupDataStreamHandler extends DataStreamHandler<StudentGroup> {

    /**
     * Constructs a new StudentDataStreamHandler with the specified file path.
     *
     * @param filePath the path to the file where grade data will be stored
     */
    StudentGroupDataStreamHandler(String filePath) {
        super(filePath);
    }

    /**
     * Reads a StudentGroup object from the input stream.
     *
     * @param in the input stream to read from
     * @return the StudentGroup object read from the stream, or null if end of file is reached
     * @throws IOException if an I/O error occurs while reading from the stream
     */
    @Override
    StudentGroup readObject(DataInputStream in) throws IOException {
        try {
            UUID id = readUuid(in);
            String code = in.readUTF();
            String specialization = in.readUTF();
            String description = in.readUTF();

            return new StudentGroup(id, code, specialization, description);
        } catch (EOFException e) {
            return null;
        }
    }

    /**
     * Writes a StudentGroup object to the output stream.
     *
     * @param out   the output stream to write to
     * @param studentGroup the StudentGroup object to write
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    @Override
    void writeObject(DataOutputStream out, StudentGroup studentGroup) throws IOException {
        writeUuid(studentGroup.getId(), out);
        out.writeUTF(studentGroup.getCode());
        out.writeUTF(studentGroup.getSpecialization());
        out.writeUTF(studentGroup.getDescription());
    }
}
