package pl.edu.wit.studentManagement.entities;

import java.util.List;
import java.util.UUID;

public class Subject {
    private final UUID id;
    private String name;
    private List<GradeCriterion> gradeCriteria;

    public Subject(String name, List<GradeCriterion> gradeCriteria) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.gradeCriteria = gradeCriteria;
    }

    public UUID getId() {
        return id;
    }

    public List<GradeCriterion> getGradeCriteria() {
        return gradeCriteria;
    }

    public void setGradeCriteria(List<GradeCriterion> gradeCriteria) {
        this.gradeCriteria = gradeCriteria;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
