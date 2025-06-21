package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A file-based implementation of {@link DataStreamHandler} for the {@link GradeCriterion} entity.
 * <p>
 * This class manages the persistence of {@link GradeCriterion} objects by serializing
 * and deserializing a list of them to a file.
 * <p>
 * It supports CRUD operations by loading all objects into memory, modifying the list,
 * and writing it back to ensure data consistency.
 * <p>
 * The file is created if it does not exist upon instantiation and initialized with an empty list.
 * <p>
 * Usage example:
 * <pre>{@code
 * DataStreamHandler<GradeCriterion> handler = new GradeCriterionDataStreamHandler("criteria.dat");
 * handler.write(new GradeCriterion(...));
 * List<GradeCriterion> all = handler.readAll();
 * }</pre>
 *
 * @author Michał Zawadzki
 */
class GradeCriterionDataStreamHandler extends DataStreamHandler<GradeCriterion> {
    private final File file;

    /**
     * Constructs a new handler for the given file path.
     * Creates the file if it does not exist and initializes it with an empty list.
     *
     * @param filePath path to the file used for persistence
     * @throws RuntimeException if the file cannot be created or initialized
     */
    GradeCriterionDataStreamHandler(String filePath) {
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
     * Reads and deserializes all {@link GradeCriterion} objects from the file.
     *
     * @return list of GradeCriterion objects, empty if file is empty
     * @throws IOException if the file cannot be read or contains invalid data
     */
    @SuppressWarnings("unchecked")
    private List<GradeCriterion> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<GradeCriterion>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    /**
     * Serializes and writes the entire list of GradeCriterion objects to the file.
     *
     * @param criteria list of GradeCriterion objects to write
     * @throws IOException if the write operation fails
     */
    private void writeAll(List<GradeCriterion> criteria) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(criteria);
        }
    }

    /**
     * Returns a copy of all GradeCriterion objects stored in the file.
     *
     * @return list of GradeCriterion objects
     * @throws IOException if the file cannot be read
     */
    @Override
    List<GradeCriterion> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    /**
     * Writes a new GradeCriterion to the file.
     *
     * @param criterion the GradeCriterion to write
     * @throws IOException if the write operation fails
     */
    @Override
    void write(GradeCriterion criterion) throws IOException {
        List<GradeCriterion> criteria = readAllInternal();
        criteria.add(criterion);
        writeAll(criteria);
    }

    /**
     * Updates an existing GradeCriterion in the file.
     * The GradeCriterion is matched by its ID.
     *
     * @param criterion the updated GradeCriterion object
     * @throws IOException if the GradeCriterion does not exist or if writing fails
     */
    @Override
    void update(GradeCriterion criterion) throws IOException {
        List<GradeCriterion> criteria = readAllInternal();
        boolean updated = false;
        for (int i = 0; i < criteria.size(); i++) {
            if (criteria.get(i).getId().equals(criterion.getId())) {
                criteria.set(i, criterion);
                updated = true;
                break;
            }
        }
        if (!updated) throw new IOException("GradeCriterion not found: " + criterion.getId());
        writeAll(criteria);
    }

    /**
     * Deletes a GradeCriterion from the file using its ID.
     *
     * @param id the UUID of the GradeCriterion to delete
     * @throws IOException if the GradeCriterion does not exist or if writing fails
     */
    @Override
    void deleteById(UUID id) throws IOException {
        List<GradeCriterion> criteria = readAllInternal();
        boolean removed = criteria.removeIf(c -> c.getId().equals(id));
        if (!removed) throw new IOException("GradeCriterion not found: " + id);
        writeAll(criteria);
    }
}
