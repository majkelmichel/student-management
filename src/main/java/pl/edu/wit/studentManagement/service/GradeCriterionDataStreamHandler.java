package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A file-based implementation of {@link DataStreamHandler} for the {@link GradeCriterion} entity.
 * <p>
 * This class manages the persistence of {@link GradeCriterion} objects by serializing
 * and deserializing a list of them to a file.
 * <p>
 * It supports CRUD operations by loading all objects into memory, modifying the list,
 * and writing it back to ensure data consistency.
 * <p>
 * The file is created if it does not exist upon instantiation and initialized with an empty list.
 * <p>
 * Usage example:
 * <pre>{@code
 * DataStreamHandler<GradeCriterion> handler = new GradeCriterionDataStreamHandler("criteria.dat");
 * handler.write(new GradeCriterion(...));
 * List<GradeCriterion> all = handler.readAll();
 * }</pre>
 *
 * @author Michał Zawadzki
 */
class GradeCriterionDataStreamHandler extends DataStreamHandler<GradeCriterion> {
    GradeCriterionDataStreamHandler(String filePath) {
        super(filePath);
    }

    @Override
    GradeCriterion readObject(DataInputStream in) throws IOException {
        try {
            UUID id = readUuid(in);
            String name = in.readUTF();
            byte maxPoints = in.readByte();
            UUID subjectId = readUuid(in);

            return new GradeCriterion(id, name, maxPoints, subjectId);
        } catch (EOFException e) {
            return null;
        }
    }

    @Override
    void writeObject(DataOutputStream out, GradeCriterion gradeCriterion) throws IOException {
        writeUuid(gradeCriterion.getId(), out);
        out.writeUTF(gradeCriterion.getName());
        out.writeByte(gradeCriterion.getMaxPoints());
        writeUuid(gradeCriterion.getSubjectId(), out);
    }
}
