package pl.edu.wit.studentManagement.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class SubjectDataStreamHandler extends DataStreamHandler<Subject> {
    private final File file;

    SubjectDataStreamHandler(String filePath) {
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

    private List<Subject> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Subject>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    private void writeAll(List<Subject> subjects) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(subjects);
        }
    }

    @Override
    List<Subject> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    @Override
    void write(Subject subject) throws IOException {
        List<Subject> subjects = readAllInternal();
        subjects.add(subject);
        writeAll(subjects);
    }

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

    @Override
    void deleteById(UUID id) throws IOException {
        List<Subject> subjects = readAllInternal();
        boolean removed = subjects.removeIf(s -> s.getId().equals(id));
        if (!removed) throw new IOException("Subject not found: " + id);
        writeAll(subjects);
    }
}
