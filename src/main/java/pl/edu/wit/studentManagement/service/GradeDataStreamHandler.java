package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.UUID;

/**
 * Implementation of {@link DataStreamHandler} for the {@link Grade} entity.
 * <p>
 * This class manages the persistence of {@link Grade} objects by serializing
 * and deserializing a list of them to a file.
 * <p>
 * It supports CRUD operations by loading all objects into memory, modifying the list,
 * and writing it back to ensure data consistency.
 * <p>
 * The file is created if it does not exist upon instantiation and initialized with an empty list.
 * <p>
 * Usage example:
 * <pre>{@code
 * DataStreamHandler<Grade> handler = new GradeDataStreamHandler("grades.dat");
 * handler.write(new Grade(...));
 * List<Grade> all = handler.readAll();
 * }</pre>
 *
 * @author Michał Zawadzki
 */
class GradeDataStreamHandler extends DataStreamHandler<Grade> {

    /**
     * Constructs a new GradeDataStreamHandler with the specified file path.
     *
     * @param filePath the path to the file where grade data will be stored
     */
    GradeDataStreamHandler(String filePath) {
        super(filePath);
    }

    /**
     * Reads a Grade object from the input stream.
     *
     * @param in the input stream to read from
     * @return the Grade object read from the stream, or null if end of file is reached
     * @throws IOException if an I/O error occurs while reading from the stream
     */
    @Override
    Grade readObject(DataInputStream in) throws IOException {
        try {
            UUID id = readUuid(in);
            UUID subjectId = readUuid(in);
            UUID gradeCriterionId = readUuid(in);
            UUID studentId = readUuid(in);
            byte grade = in.readByte();

            return new Grade(id, subjectId, gradeCriterionId, studentId, grade);
        } catch (EOFException e) {
            return null;
        }
    }

    /**
     * Writes a Grade object to the output stream.
     *
     * @param out   the output stream to write to
     * @param grade the Grade object to write
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    @Override
    void writeObject(DataOutputStream out, Grade grade) throws IOException {
        writeUuid(grade.getId(), out);
        writeUuid(grade.getSubjectId(), out);
        writeUuid(grade.getGradeCriterionId(), out);
        writeUuid(grade.getStudentId(), out);
        out.writeByte(grade.getGrade());
    }
}