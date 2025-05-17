package pl.edu.wit.studentManagement.persistence.interfaces;

import java.io.IOException;
import java.util.List;

public interface DataStreamHandler<T> {
    List<T> readAll() throws IOException;
    void write(T object) throws IOException;
    void update(T object) throws IOException;
    void deleteById(int id) throws IOException;
}
