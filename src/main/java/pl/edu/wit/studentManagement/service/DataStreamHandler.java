package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

abstract class DataStreamHandler<T extends Entity> {
    private final String filePath;
    
    protected DataStreamHandler(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * Reads all objects of type T from the data stream.
     *
     * @return a list of all objects read
     * @throws IOException if an I/O error occurs during reading
     */
    List<T> readAll() throws IOException {
        List<T> objects = new ArrayList<>();
        File file = new File(filePath);
        
        if (!file.exists()) {
            return objects;
        }
        
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            while (in.available() > 0) {
                T object = readObject(in);
                if (object != null) {
                    objects.add(object);
                }
            }
        }
        return objects;
    }

    /**
     * Writes a new object to the data stream.
     *
     * @param object the object to write
     * @throws IOException if an I/O error occurs during writing
     */
    void write(T object) throws IOException {
        List<T> existingObjects = readAll();
        existingObjects.add(object);
        writeAll(existingObjects);
    }

    /**
     * Updates an existing object in the data stream.
     *
     * @param object the object with updated data
     * @throws IOException if an I/O error occurs during updating
     */
    void update(T object) throws IOException {
        List<T> objects = readAll();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getId().equals(object.getId())) {
                objects.set(i, object);
                writeAll(objects);
                return;
            }
        }
        throw new IOException("Object not found with ID: " + object.getId());
    }

    /**
     * Deletes an object identified by its UUID from the data stream.
     *
     * @param id the UUID of the object to delete
     * @throws IOException if an I/O error occurs during deletion
     */
    void deleteById(UUID id) throws IOException {
        List<T> objects = readAll();
        if (objects.removeIf(obj -> obj.getId().equals(id))) {
            writeAll(objects);
        } else {
            throw new IOException("Object not found with ID: " + id);
        }
    }

    /**
     * Writes all objects to the file.
     *
     * @param objects list of objects to write
     * @throws IOException if an I/O error occurs during writing
     */
    void writeAll(List<T> objects) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath))) {
            for (T object : objects) {
                writeObject(out, object);
            }
        }
    }

    /**
     * Writes a UUID to the data output stream.
     * If the UUID is null, writes a boolean flag indicating null value.
     * Otherwise, writes the UUID's most and least significant bits.
     *
     * @param uuid the UUID to write, can be null
     * @param out  the output stream to write to
     * @throws IOException if an I/O error occurs during writing
     */
    void writeUuid(UUID uuid, DataOutputStream out) throws IOException {
        if (uuid == null) {
            out.writeBoolean(false);
            return;
        }
        out.writeBoolean(true);
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }

    /**
     * Reads a UUID from the data input stream.
     * First reads a boolean flag indicating if the UUID is null.
     * If not null, reads the most and least significant bits to construct the UUID.
     *
     * @param in the input stream to read from
     * @return the UUID read from the stream, or null if the stored value was null
     * @throws IOException if an I/O error occurs during reading
     */
    UUID readUuid(DataInputStream in) throws IOException {
        if (!in.readBoolean()) {
            return null;
        }
        return new UUID(in.readLong(), in.readLong());
    }

    /**
     * Reads a single object from the input stream.
     * Must be implemented by concrete classes to handle specific entity types.
     *
     * @param in DataInputStream to read from
     * @return the object read from the stream
     * @throws IOException if an I/O error occurs during reading
     */
    abstract T readObject(DataInputStream in) throws IOException;

    /**
     * Writes a single object to the output stream.
     * Must be implemented by concrete classes to handle specific entity types.
     *
     * @param out DataOutputStream to write to
     * @param object the object to write
     * @throws IOException if an I/O error occurs during writing
     */
    abstract void writeObject(DataOutputStream out, T object) throws IOException;
}