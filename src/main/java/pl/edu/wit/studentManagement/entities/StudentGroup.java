package pl.edu.wit.studentManagement.entities;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.UUID;

public class StudentGroup {
    private final UUID id;
    private String code;
    private String specialization;
    private String description;
    private List<Student> students;
    private List<Subject> subjects;

    public StudentGroup(String code, String specialization, String description, List<Student> students, List<Subject> subjects) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.specialization = specialization;
        this.description = description;
        this.students = students;
        this.subjects = subjects;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void validate() throws ValidationException {
    }
}
