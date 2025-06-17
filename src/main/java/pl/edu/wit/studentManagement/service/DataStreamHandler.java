package pl.edu.wit.studentManagement.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

abstract class DataStreamHandler<T> {
    abstract List<T> readAll() throws IOException;
    abstract void write(T object) throws IOException;
    abstract void update(T object) throws IOException;
    abstract void deleteById(UUID id) throws IOException;
}
