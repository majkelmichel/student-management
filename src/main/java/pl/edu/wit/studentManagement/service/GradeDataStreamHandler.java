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
 * The file format used is Java's built-in object serialization.
 * All {@link Grade} objects must implement {@link Serializable}.
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
    private final File file;

    /**
     * Constructs a new handler for the given file path.
     * Creates the file if it does not exist and initializes it with an empty list.
     *
     * @param filePath path to the file used for persistence
     * @throws RuntimeException if the file cannot be created or initialized
     */
    GradeDataStreamHandler(String filePath) {
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
     * Reads and deserializes all {@link Grade} objects from the file.
     *
     * @return list of grades, empty if file is empty
     * @throws IOException if the file cannot be read or contains invalid data
     */
    @SuppressWarnings("unchecked")
    private List<Grade> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Grade>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    /**
     * Serializes and writes the entire list of grades to the file.
     *
     * @param grades list of grades to write
     * @throws IOException if the write operation fails
     */
    private void writeAll(List<Grade> grades) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(grades);
        }
    }

    /**
     * Returns a copy of all grades stored in the file.
     *
     * @return list of grades
     * @throws IOException if the file cannot be read
     */
    @Override
    List<Grade> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    /**
     * Writes a new grade to the file.
     *
     * @param grade the grade to write
     * @throws IOException if the write operation fails
     */
    @Override
    void write(Grade grade) throws IOException {
        List<Grade> grades = readAllInternal();
        grades.add(grade);
        writeAll(grades);
    }

    /**
     * Updates an existing grade in the file.
     * The grade is matched by its ID.
     *
     * @param grade the updated grade object
     * @throws IOException if the grade does not exist or if writing fails
     */
    @Override
    void update(Grade grade) throws IOException {
        List<Grade> grades = readAllInternal();
        boolean updated = false;
        for (int i = 0; i < grades.size(); i++) {
            if (grades.get(i).getId().equals(grade.getId())) {
                grades.set(i, grade);
                updated = true;
                break;
            }
        }
        if (!updated) throw new IOException("Grade not found: " + grade.getId());
        writeAll(grades);
    }

    /**
     * Deletes a grade from the file using its ID.
     *
     * @param id the UUID of the grade to delete
     * @throws IOException if the grade does not exist or if writing fails
     */
    @Override
    void deleteById(UUID id) throws IOException {
        List<Grade> grades = readAllInternal();
        boolean removed = grades.removeIf(g -> g.getId().equals(id));
        if (!removed) throw new IOException("Grade not found: " + id);
        writeAll(grades);
    }
}
