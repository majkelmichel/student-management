package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

abstract class Dao<T> {
    abstract Optional<T> get(UUID id);
    abstract List<T> getAll();
    abstract boolean save(T t) throws ValidationException;
    abstract boolean update(T t) throws ValidationException;
    abstract boolean delete(UUID id);
}
