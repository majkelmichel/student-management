package pl.edu.wit.studentManagement.dao.interfaces;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(int id);
    List<T> getAll();
    boolean save(T t) throws ValidationException;
    boolean update(T t) throws ValidationException;
    boolean delete(int id);
}
