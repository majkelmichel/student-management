package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.UUID;

/**
 * Implementation of {@link Student} for the {@link Student} entity.
 * <p>
 * This class manages the persistence of {@link Student} objects by serializing
 * and deserializing a list of them to a file.
 * <p>
 * It supports CRUD operations by loading all objects into memory, modifying the list,
 * and writing it back to ensure data consistency.
 * <p>
 * The file is created if it does not exist upon instantiation and initialized with an empty list.
 * <p>
 * Usage example:
 * <pre>{@code
 * DataStreamHandler<Student> handler = new StudentDataStreamHandler("students.dat");
 * handler.write(new Student(...));
 * List<Student> all = handler.readAll();
 * }</pre>
 *
 * @author Michał Zawadzki
 */
class StudentDataStreamHandler extends DataStreamHandler<Student> {

    /**
     * Constructs a new StudentDataStreamHandler with the specified file path.
     *
     * @param filePath the path to the file where grade data will be stored
     */
    StudentDataStreamHandler(String filePath) {
        super(filePath);
    }

    /**
     * Reads a Student object from the input stream.
     *
     * @param in the input stream to read from
     * @return the Student object read from the stream, or null if end of file is reached
     * @throws IOException if an I/O error occurs while reading from the stream
     */
    @Override
    Student readObject(DataInputStream in) throws IOException {
        try {
            UUID id = readUuid(in);
            String firstName = in.readUTF();
            String lastName = in.readUTF();
            String album = in.readUTF();
            UUID studentGroupId = readUuid(in);

            return new Student(id, firstName, lastName, album, studentGroupId);
        } catch (EOFException e) {
            return null;
        }
    }

    /**
     * Writes a Student object to the output stream.
     *
     * @param out   the output stream to write to
     * @param student the Grade object to write
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    @Override
    void writeObject(DataOutputStream out, Student student) throws IOException {
        writeUuid(student.getId(), out);
        out.writeUTF(student.getFirstName());
        out.writeUTF(student.getLastName());
        out.writeUTF(student.getAlbum());
        writeUuid(student.getStudentGroupId(), out);
    }
}
