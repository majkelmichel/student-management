package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A file-based implementation of {@link DataStreamHandler} for the {@link Subject} entity.
 * <p>
 * This class manages the persistence of {@link Subject} objects by serializing
 * and deserializing them to and from a file on disk.
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
    SubjectDataStreamHandler(String filePath) {
        super(filePath);
    }

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

    @Override
    void writeObject(DataOutputStream out, Subject object) throws IOException {
        writeUuid(object.getId(), out);
        out.writeUTF(object.getName());
    }
}
