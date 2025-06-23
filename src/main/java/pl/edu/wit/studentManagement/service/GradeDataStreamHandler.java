package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A file-based implementation of {@link DataStreamHandler} for the {@link Grade} entity.
 * <p>
 * This class handles reading, writing, updating, and deleting {@link Grade} objects
 * by serializing them to and deserializing them from a file on disk.
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

    GradeDataStreamHandler(String filePath) {
        super(filePath);
    }

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

    @Override
    void writeObject(DataOutputStream out, Grade grade) throws IOException {
        writeUuid(grade.getId(), out);
        writeUuid(grade.getSubjectId(), out);
        writeUuid(grade.getGradeCriterionId(), out);
        writeUuid(grade.getStudentId(), out);
        out.writeByte(grade.getGrade());
    }
}