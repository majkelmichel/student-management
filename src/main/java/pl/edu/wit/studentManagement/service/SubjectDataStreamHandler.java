package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A file-based implementation of {@link DataStreamHandler} for the {@link Subject} entity.
 * <p>
 * This class handles reading, writing, updating, and deleting {@link Subject} objects
 * by serializing them to and deserializing them from a file on disk.
 * <p>
 * The file format used is Java's built-in object serialization.
 * All {@link Subject} objects must implement {@link Serializable}.
 * <p>
 * The file is created if it does not exist upon instantiation and initialized with an empty list.
 * <p>
 * Usage example:
 * <pre>{@code
 * DataStreamHandler<Subject> handler = new SubjectDataStreamHandler("subjects.dat");
 * handler.write(new Subject(...));
 * List<Subject> all = handler.readAll();
 * }</pre>
 *
 * @author Michał Zawadzki
 */
class SubjectDataStreamHandler extends DataStreamHandler<Subject> {
    private final File file;

    /**
     * Constructs a new handler for the given file path.
     * Creates the file if it does not exist and initializes it with an empty list.
     *
     * @param filePath path to the file used for persistence
     * @throws RuntimeException if the file cannot be created or initialized
     */
    SubjectDataStreamHandler(String filePath) {
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
     * Reads and deserializes all {@link Subject} objects from the file.
     *
     * @return list of subjects, empty if file is empty
     * @throws IOException if the file cannot be read or contains invalid data
     */
    @SuppressWarnings("unchecked")
    private List<Subject> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Subject>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    /**
     * Serializes and writes the entire list of subjects to the file.
     *
     * @param subjects list of subjects to write
     * @throws IOException if the write operation fails
     */
    private void writeAll(List<Subject> subjects) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(subjects);
        }
    }

    /**
     * Returns a copy of all subjects stored in the file.
     *
     * @return list of subjects
     * @throws IOException if the file cannot be read
     */
    @Override
    List<Subject> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    /**
     * Writes a new subject to the file.
     *
     * @param subject the subject to write
     * @throws IOException if the write operation fails
     */
    @Override
    void write(Subject subject) throws IOException {
        List<Subject> subjects = readAllInternal();
        subjects.add(subject);
        writeAll(subjects);
    }

    /**
     * Updates an existing subject in the file by matching its ID.
     *
     * @param subject the updated subject object
     * @throws IOException if the subject does not exist or writing fails
     */
    @Override
    void update(Subject subject) throws IOException {
        List<Subject> subjects = readAllInternal();
        boolean updated = false;
        for (int i = 0; i < subjects.size(); i++) {
            if (subjects.get(i).getId().equals(subject.getId())) {
                subjects.set(i, subject);
                updated = true;
                break;
            }
        }
        if (!updated) throw new IOException("Subject not found: " + subject.getId());
        writeAll(subjects);
    }

    /**
     * Deletes a subject from the file by its ID.
     *
     * @param id UUID of the subject to delete
     * @throws IOException if the subject does not exist or writing fails
     */
    @Override
    void deleteById(UUID id) throws IOException {
        List<Subject> subjects = readAllInternal();
        boolean removed = subjects.removeIf(s -> s.getId().equals(id));
        if (!removed) throw new IOException("Subject not found: " + id);
        writeAll(subjects);
    }
}
