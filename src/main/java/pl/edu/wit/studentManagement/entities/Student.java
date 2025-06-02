package pl.edu.wit.studentManagement.entities;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.UUID;

/**
 * @author Michał Zawadzki
 */
public class Student {
    private final UUID id;
    private String firstName;
    private String lastName;
    private String album;

    public Student(UUID id, String firstName, String lastName, String album) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void validate() throws ValidationException {
    }
}
