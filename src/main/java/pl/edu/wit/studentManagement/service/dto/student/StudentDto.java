package pl.edu.wit.studentManagement.service.dto.student;

import java.util.UUID;

public class StudentDto {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String album;

    public StudentDto(UUID id, String firstName, String lastName, String album) {
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

    public String getLastName() {
        return lastName;
    }

    public String getAlbum() {
        return album;
    }
}
