package pl.edu.wit.studentManagement.dao.interfaces;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Dao<T> {
    Optional<T> get(UUID id);
    List<T> getAll();
    boolean save(T t) throws ValidationException;
    boolean update(T t) throws ValidationException;
    boolean delete(UUID id);
}