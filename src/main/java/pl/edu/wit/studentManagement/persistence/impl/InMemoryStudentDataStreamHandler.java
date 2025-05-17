package pl.edu.wit.studentManagement.persistence.impl;

import pl.edu.wit.studentManagement.persistence.interfaces.DataStreamHandler;
import pl.edu.wit.studentManagement.entities.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InMemoryStudentDataStreamHandler implements DataStreamHandler<Student> {
    private final ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();

    @Override
    public List<Student> readAll() throws IOException {
        List<Student> students = new ArrayList<>();
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()))) {
            while (in.available() > 0) {
                int id = in.readInt();
                String firstName = in.readUTF();
                String lastName = in.readUTF();
                String album = in.readUTF();

                students.add(new Student(id, firstName, lastName, album));
            }
        }
        return students;
    }

    @Override
    public void write(Student student) throws IOException {
        List<Student> students = readAll();
        students.add(student);
        saveAll(students);
    }

    @Override
    public void update(Student student) throws IOException {
        List<Student> students = readAll();
        boolean updated = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == student.getId()) {
                students.set(i, student);
                updated = true;
                break;
            }
        }
        if (!updated) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " not found.");
        }
        saveAll(students);
    }

    @Override
    public void deleteById(int id) throws IOException {
        List<Student> students = readAll();
        students.removeIf(s -> s.getId() == id);
        saveAll(students);
    }

    private void saveAll(List<Student> students) throws IOException {
        memoryBuffer.reset();
        try (DataOutputStream out = new DataOutputStream(memoryBuffer)) {
            for (Student s : students) {
                out.writeInt(s.getId());
                out.writeUTF(s.getFirstName());
                out.writeUTF(s.getLastName());
                out.writeUTF(s.getAlbum());
            }
        }
    }
}
