package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A file-based implementation of {@link DataStreamHandler} for the {@link StudentGroup} entity.
 * <p>
 * This class handles reading, writing, updating, and deleting {@link StudentGroup} objects
 * by serializing them to and deserializing them from a file on disk.
 * <p>
 * The file format used is Java's built-in object serialization.
 * All {@link StudentGroup} objects must implement {@link Serializable}.
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
    private final File file;

    /**
     * Constructs a new handler for the given file path.
     * Creates the file if it does not exist and initializes it with an empty list.
     *
     * @param filePath path to the file used for persistence
     * @throws RuntimeException if the file cannot be created or initialized
     */
    StudentGroupDataStreamHandler(String filePath) {
        this.file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                writeAll(new ArrayList<>()); // initialize with empty list
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize file", e);
            }
        }
    }

    /**
     * Reads and deserializes all {@link StudentGroup} objects from the file.
     *
     * @return list of student groups, empty if file is empty
     * @throws IOException if the file cannot be read or contains invalid data
     */
    @SuppressWarnings("unchecked")
    private List<StudentGroup> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<StudentGroup>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    /**
     * Serializes and writes the entire list of student groups to the file.
     *
     * @param groups list of student groups to write
     * @throws IOException if the write operation fails
     */
    private void writeAll(List<StudentGroup> groups) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(groups);
        }
    }

    /**
     * Returns a copy of all student groups stored in the file.
     *
     * @return list of student groups
     * @throws IOException if the file cannot be read
     */
    @Override
    List<StudentGroup> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    /**
     * Writes a new student group to the file.
     *
     * @param group the student group to write
     * @throws IOException if the write operation fails
     */
    @Override
    void write(StudentGroup group) throws IOException {
        List<StudentGroup> groups = readAllInternal();
        groups.add(group);
        writeAll(groups);
    }

    /**
     * Updates an existing student group in the file by matching its ID.
     *
     * @param group the updated student group object
     * @throws IOException if the student group does not exist or writing fails
     */
    @Override
    void update(StudentGroup group) throws IOException {
        List<StudentGroup> groups = readAllInternal();
        boolean updated = false;
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getId().equals(group.getId())) {
                groups.set(i, group);
                updated = true;
                break;
            }
        }
        if (!updated) throw new IOException("StudentGroup not found: " + group.getId());
        writeAll(groups);
    }

    /**
     * Deletes a student group from the file by its ID.
     *
     * @param id UUID of the student group to delete
     * @throws IOException if the student group does not exist or writing fails
     */
    @Override
    void deleteById(UUID id) throws IOException {
        List<StudentGroup> groups = readAllInternal();
        boolean removed = groups.removeIf(g -> g.getId().equals(id));
        if (!removed) throw new IOException("StudentGroup not found: " + id);
        writeAll(groups);
    }
}
