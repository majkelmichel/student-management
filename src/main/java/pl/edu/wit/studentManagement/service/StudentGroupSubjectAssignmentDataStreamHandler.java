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
    private final File file;

    /**
     * Constructs a data stream handler for the specified file path.
     * Creates the file and initializes it with an empty list if it does not exist.
     *
     * @param filePath the file path to persist assignments
     * @throws RuntimeException if the file cannot be created or initialized
     */
    StudentGroupSubjectAssignmentDataStreamHandler(String filePath) {
        this.file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                writeAll(new ArrayList<>());
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize file", e);
            }
        }
    }

    /**
     * Reads all assignments from the file.
     * Returns an empty list if the file is empty.
     *
     * @return list of all {@link StudentGroupSubjectAssignment} stored
     * @throws IOException if reading the file or data deserialization fails
     */
    @SuppressWarnings("unchecked")
    private List<StudentGroupSubjectAssignment> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<StudentGroupSubjectAssignment>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    /**
     * Writes the entire list of assignments to the file, replacing existing content.
     *
     * @param assignments list of assignments to persist
     * @throws IOException if writing to the file fails
     */
    private void writeAll(List<StudentGroupSubjectAssignment> assignments) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(assignments);
        }
    }

    @Override
    List<StudentGroupSubjectAssignment> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    @Override
    void write(StudentGroupSubjectAssignment assignment) throws IOException {
        List<StudentGroupSubjectAssignment> assignments = readAllInternal();
        assignments.add(assignment);
        writeAll(assignments);
    }

    @Override
    void update(StudentGroupSubjectAssignment assignment) throws IOException {
        List<StudentGroupSubjectAssignment> assignments = readAllInternal();
        boolean updated = false;
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).getId().equals(assignment.getId())) {
                assignments.set(i, assignment);
                updated = true;
                break;
            }
        }
        if (!updated) throw new IOException("Assignment not found: " + assignment.getId());
        writeAll(assignments);
    }

    @Override
    void deleteById(UUID id) throws IOException {
        List<StudentGroupSubjectAssignment> assignments = readAllInternal();
        boolean removed = assignments.removeIf(a -> a.getId().equals(id));
        if (!removed) throw new IOException("Assignment not found: " + id);
        writeAll(assignments);
    }
}
