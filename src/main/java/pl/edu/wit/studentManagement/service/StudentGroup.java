package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.Serializable;
import java.util.UUID;

class StudentGroup implements Serializable {
    private final UUID id;
    private String code;
    private String specialization;
    private String description;

    StudentGroup(String code, String specialization, String description) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.specialization = specialization;
        this.description = description;
    }

    UUID getId() {
        return id;
    }

    String getCode() {
        return code;
    }

    void setCode(String code) {
        this.code = code;
    }

    String getSpecialization() {
        return specialization;
    }

    void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void validate() throws ValidationException {
    }
}
