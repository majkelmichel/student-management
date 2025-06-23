package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.UUID;

/**
 * Implementation of {@link DataStreamHandler} for the {@link GradeCriterion} entity.
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
    /**
     * Constructs a new GradeCriterionDataStreamHandler with the specified file path.
     *
     * @param filePath the path to the file where grade criteria will be stored
     */
    GradeCriterionDataStreamHandler(String filePath) {
        super(filePath);
    }

    /**
     * Reads a GradeCriterion object from the input stream.
     *
     * @param in the input stream to read from
     * @return the GradeCriterion object read from the stream, or null if end of file is reached
     * @throws IOException if an I/O error occurs while reading from the stream
     */
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

    /**
     * Writes a GradeCriterion object to the output stream.
     *
     * @param out            the output stream to write to
     * @param gradeCriterion the GradeCriterion object to write
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    @Override
    void writeObject(DataOutputStream out, GradeCriterion gradeCriterion) throws IOException {
        writeUuid(gradeCriterion.getId(), out);
        out.writeUTF(gradeCriterion.getName());
        out.writeByte(gradeCriterion.getMaxPoints());
        writeUuid(gradeCriterion.getSubjectId(), out);
    }
}
