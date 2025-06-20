package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File-based data handler for persisting {@link StudentGroupSubjectAssignment} entities using Java serialization.
 * Manages all assignments in a single file.
 *
 * @author Michał Zawadzki
 */
class StudentGroupSubjectAssignmentDataStreamHandler extends DataStreamHandler<StudentGroupSubjectAssignment> {
    private final File file;

    /**
     * Constructs a new handler for the given file path.
     * Creates and initializes the file if it does not exist.
     *
     * @param filePath path to the file used for persistence
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
