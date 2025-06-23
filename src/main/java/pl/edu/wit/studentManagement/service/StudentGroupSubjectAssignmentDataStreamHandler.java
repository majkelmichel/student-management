package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File-based data stream handler for persisting {@link StudentGroupSubjectAssignment} entities using Java serialization.
 * <p>
 * Manages all student group-subject assignments within a single file.
 * Supports reading all assignments, writing new assignments, updating existing ones, and deleting by ID.
 * Ensures the data file is initialized if it does not exist.
 *
 * @author Michał Zawadzki
 */
class StudentGroupSubjectAssignmentDataStreamHandler extends DataStreamHandler<StudentGroupSubjectAssignment> {
    StudentGroupSubjectAssignmentDataStreamHandler(String filePath) {
        super(filePath);
    }

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

    @Override
    void writeObject(DataOutputStream out, StudentGroupSubjectAssignment object) throws IOException {
        writeUuid(object.getId(), out);
        writeUuid(object.getStudentGroupId(), out);
        writeUuid(object.getSubjectId(), out);
    }
}
