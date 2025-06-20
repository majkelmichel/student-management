package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A file-based implementation of {@link DataStreamHandler} for the {@link Student} entity.
 * <p>
 * This class handles reading, writing, updating, and deleting {@link Student} objects
 * by serializing them to and deserializing them from a file on disk.
 * <p>
 * The file format used is Java's built-in object serialization.
 * All {@link Student} objects must implement {@link Serializable}.
 * <p>
 * The file is created if it does not exist upon instantiation and initialized with an empty list.
 * <p>
 * Usage example:
 * <pre>{@code
 * DataStreamHandler<Student> handler = new StudentDataStreamHandler("students.dat");
 * handler.write(new Student(...));
 * List<Student> all = handler.readAll();
 * }</pre>
 *
 * @author Michał Zawadzki
 */
class StudentDataStreamHandler extends DataStreamHandler<Student> {
    private final File file;

    /**
     * Constructs a new handler for the given file path.
     * Creates the file if it does not exist and initializes it with an empty list.
     *
     * @param filePath path to the file used for persistence
     * @throws RuntimeException if the file cannot be created or initialized
     */
    StudentDataStreamHandler(String filePath) {
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
     * Reads and deserializes all {@link Student} objects from the file.
     *
     * @return list of students, empty if file is empty
     * @throws IOException if the file cannot be read or contains invalid data
     */
    @SuppressWarnings("unchecked")
    private List<Student> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Student>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    /**
     * Serializes and writes the entire list of students to the file.
     *
     * @param students list of students to write
     * @throws IOException if the write operation fails
     */
    private void writeAll(List<Student> students) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(students);
        }
    }

    /**
     * Returns a copy of all students stored in the file.
     *
     * @return list of students
     * @throws IOException if the file cannot be read
     */
    @Override
    List<Student> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    /**
     * Writes a new student to the file.
     *
     * @param student the student to write
     * @throws IOException if the write operation fails
     */
    @Override
    void write(Student student) throws IOException {
        List<Student> students = readAllInternal();
        students.add(student);
        writeAll(students);
    }

    /**
     * Updates an existing student in the file by matching its ID.
     *
     * @param student the updated student object
     * @throws IOException if the student does not exist or writing fails
     */
    @Override
    void update(Student student) throws IOException {
        List<Student> students = readAllInternal();
        boolean updated = false;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(student.getId())) {
                students.set(i, student);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new IOException("Student with ID " + student.getId() + " not found.");
        }

        writeAll(students);
    }

    /**
     * Deletes a student from the file by its ID.
     *
     * @param id UUID of the student to delete
     * @throws IOException if the student does not exist or writing fails
     */
    @Override
    void deleteById(UUID id) throws IOException {
        List<Student> students = readAllInternal();
        boolean removed = students.removeIf(s -> s.getId().equals(id));

        if (!removed) {
            throw new IOException("Student with ID " + id + " not found.");
        }

        writeAll(students);
    }
}
