package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A file-based implementation of {@link DataStreamHandler} for the {@link StudentGroup} entity.
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
    StudentGroupDataStreamHandler(String filePath) {
        super(filePath);
    }

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

    @Override
    void writeObject(DataOutputStream out, StudentGroup object) throws IOException {
        writeUuid(object.getId(), out);
        out.writeUTF(object.getCode());
        out.writeUTF(object.getSpecialization());
        out.writeUTF(object.getDescription());
    }
}
