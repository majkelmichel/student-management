package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link StudentGroupSubjectAssignment} for the {@link StudentGroupSubjectAssignment} entity.
 * <p>
 * This class manages the persistence of {@link StudentGroupSubjectAssignment} objects by serializing
 * and deserializing a list of them to a file.
 * <p>
 * It supports CRUD operations by loading all objects into memory, modifying the list,
 * and writing it back to ensure data consistency.
 * <p>
 * The file is created if it does not exist upon instantiation and initialized with an empty list.
 * <p>
 * Usage example:
 * <pre>{@code
 * DataStreamHandler<StudentGroupSubjectAssignment> handler = new StudentGroupDataStreamHandler("assignment.dat");
 * handler.write(new StudentGroupSubjectAssignment(...));
 * List<StudentGroupSubjectAssignment> all = handler.readAll();
 * }</pre>
 *
 * @author Michał Zawadzki
 */
class StudentGroupSubjectAssignmentDataStreamHandler extends DataStreamHandler<StudentGroupSubjectAssignment> {
    /**
     * Constructs a new StudentGroupSubjectAssignmentDataStreamHandler with the specified file path.
     *
     * @param filePath the path to the file where grade data will be stored
     */
    StudentGroupSubjectAssignmentDataStreamHandler(String filePath) {
        super(filePath);
    }

    /**
     * Reads a StudentGroupSubjectAssignment object from the input stream.
     *
     * @param in the input stream to read from
     * @return the StudentGroupSubjectAssignment object read from the stream, or null if end of file is reached
     * @throws IOException if an I/O error occurs while reading from the stream
     */
    @Override
    StudentGroupSubjectAssignment readObject(DataInputStream in) throws IOException {
        try {
            UUID id = readUuid(in);
            UUID studentGroupId = readUuid(in);
            UUID subjectId = readUuid(in);

            return new StudentGroupSubjectAssignment(id, studentGroupId, subjectId);
        } catch (EOFException e) {
            return null;
        }
    }

    /**
     * Writes a StudentGroupSubjectAssignment object to the output stream.
     *
     * @param out   the output stream to write to
     * @param assignment the StudentGroupSubjectAssignment object to write
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    @Override
    void writeObject(DataOutputStream out, StudentGroupSubjectAssignment assignment) throws IOException {
        writeUuid(assignment.getId(), out);
        writeUuid(assignment.getStudentGroupId(), out);
        writeUuid(assignment.getSubjectId(), out);
    }
}
