package pl.edu.wit.studentManagement.dao.impl;

import pl.edu.wit.studentManagement.dao.interfaces.Dao;
import pl.edu.wit.studentManagement.entities.StudentGroup;

import java.util.List;
import java.util.Optional;

public class StudentGroupDao implements Dao<StudentGroup> {
    // TODO: implement
    @Override
    public Optional<StudentGroup> get(int id) {
        return Optional.empty();
    }

    @Override
    public List<StudentGroup> getAll() {
        return List.of();
    }

    @Override
    public boolean save(StudentGroup studentGroup) {
        return false;
    }

    @Override
    public boolean update(StudentGroup studentGroup) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
