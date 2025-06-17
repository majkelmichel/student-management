package pl.edu.wit.studentManagement.service;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class GradeCriterionDataStreamHandler extends DataStreamHandler<GradeCriterion> {
    private final File file;

    GradeCriterionDataStreamHandler(String filePath) {
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

    private List<GradeCriterion> readAllInternal() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<GradeCriterion>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data format error", e);
        }
    }

    private void writeAll(List<GradeCriterion> criteria) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(criteria);
        }
    }

    @Override
    List<GradeCriterion> readAll() throws IOException {
        return new ArrayList<>(readAllInternal());
    }

    @Override
    void write(GradeCriterion criterion) throws IOException {
        List<GradeCriterion> criteria = readAllInternal();
        criteria.add(criterion);
        writeAll(criteria);
    }

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

    @Override
    void deleteById(UUID id) throws IOException {
        List<GradeCriterion> criteria = readAllInternal();
        boolean removed = criteria.removeIf(c -> c.getId().equals(id));
        if (!removed) throw new IOException("GradeCriterion not found: " + id);
        writeAll(criteria);
    }
}
