package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class StudentGroupDataStreamHandler extends DataStreamHandler<StudentGroup> {
    private final File file;

    StudentGroupDataStreamHandler(String filePath) {
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

    private List<StudentGroup> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<StudentGroup>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    private void writeAll(List<StudentGroup> groups) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(groups);
        }
    }

    @Override
    List<StudentGroup> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    @Override
    void write(StudentGroup group) throws IOException {
        List<StudentGroup> groups = readAllInternal();
        groups.add(group);
        writeAll(groups);
    }

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

    @Override
    void deleteById(UUID id) throws IOException {
        List<StudentGroup> groups = readAllInternal();
        boolean removed = groups.removeIf(g -> g.getId().equals(id));
        if (!removed) throw new IOException("StudentGroup not found: " + id);
        writeAll(groups);
    }
}
