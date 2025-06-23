package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A file-based implementation of {@link DataStreamHandler} for the {@link Student} entity.
 * <p>
 * This class handles reading, writing, updating, and deleting {@link Student} objects
 * by serializing them to and deserializing them from a file on disk.
 * <p>
 * The file format used is Java's built-in object serialization.
 * All {@link Student} objects must implement {@link Serializable}.
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
    StudentDataStreamHandler(String filePath) {
        super(filePath);
    }

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

    @Override
    void writeObject(DataOutputStream out, Student object) throws IOException {
        writeUuid(object.getId(), out);
        out.writeUTF(object.getFirstName());
        out.writeUTF(object.getLastName());
        out.writeUTF(object.getAlbum());
        writeUuid(object.getStudentGroupId(), out);
    }
}
