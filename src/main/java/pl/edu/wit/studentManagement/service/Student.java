package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Michał Zawadzki
 */
class Student implements Serializable {
    private final UUID id;
    private String firstName;
    private String lastName;
    private String album;
    private UUID studentGroupId;

    Student(String firstName, String lastName, String album) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
    }

    UUID getId() {
        return id;
    }

    String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String getAlbum() {
        return album;
    }

    void setAlbum(String album) {
        this.album = album;
    }

    UUID getStudentGroupId() {
        return studentGroupId;
    }

    void setStudentGroupId(UUID studentGroupId) {
        this.studentGroupId = studentGroupId;
    }

    void validate() throws ValidationException {
    }
}
