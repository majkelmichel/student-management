package pl.edu.wit.studentManagement.persistence.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DataStreamHandler<T> {
    List<T> readAll() throws IOException;
    void write(T object) throws IOException;
    void update(T object) throws IOException;
    void deleteById(UUID id) throws IOException;
}