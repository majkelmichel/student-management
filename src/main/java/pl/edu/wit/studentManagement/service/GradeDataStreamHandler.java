package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File-based data handler for Grade entities.
 */
class GradeDataStreamHandler extends DataStreamHandler<Grade> {
    private final File file;

    GradeDataStreamHandler(String filePath) {
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

    private List<Grade> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Grade>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    private void writeAll(List<Grade> grades) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(grades);
        }
    }

    @Override
    List<Grade> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    @Override
    void write(Grade grade) throws IOException {
        List<Grade> grades = readAllInternal();
        grades.add(grade);
        writeAll(grades);
    }

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

    @Override
    void deleteById(UUID id) throws IOException {
        List<Grade> grades = readAllInternal();
        boolean removed = grades.removeIf(g -> g.getId().equals(id));
        if (!removed) throw new IOException("Grade not found: " + id);
        writeAll(grades);
    }
}
