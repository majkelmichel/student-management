package pl.edu.wit.studentManagement.service;

import java.io.Serializable;
import java.util.UUID;

class Subject implements Serializable {
    private final UUID id;
    private String name;

    Subject(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    UUID getId() {
        return id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}
