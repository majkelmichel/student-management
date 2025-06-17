package pl.edu.wit.studentManagement.service;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

class Subject implements Serializable {
    private final UUID id;
    private String name;
    private List<GradeCriterion> gradeCriteria;

    Subject(String name, List<GradeCriterion> gradeCriteria) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.gradeCriteria = gradeCriteria;
    }

    UUID getId() {
        return id;
    }

    List<GradeCriterion> getGradeCriteria() {
        return gradeCriteria;
    }

    void setGradeCriteria(List<GradeCriterion> gradeCriteria) {
        this.gradeCriteria = gradeCriteria;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}
