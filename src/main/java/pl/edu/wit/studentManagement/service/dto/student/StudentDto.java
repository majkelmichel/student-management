package pl.edu.wit.studentManagement.service.dto.student;

import java.util.UUID;

/**
 * Data Transfer Object representing a student.
 * Contains immutable student data typically returned from the system.
 *
 * @author Michał Zawadzki
 */
public class StudentDto {
    /**
     * Unique identifier of the student
     */
    private final UUID id;
    /**
     * Student's first name
     */
    private final String firstName;
    /**
     * Student's last name
     */
    private final String lastName;
    /**
     * Student's album number (student ID)
     */
    private final String album;

    /**
     * Constructs a StudentDto with given student details.
     *
     * @param id unique identifier of the student
     * @param firstName student's first name
     * @param lastName student's last name
     * @param album student's album number (student ID)
     */
    public StudentDto(UUID id, String firstName, String lastName, String album) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
    }

    /**
     * Returns the unique identifier of the student.
     *
     * @return student ID as UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the student's first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the student's last name.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the student's album number (student ID).
     *
     * @return album number
     */
    public String getAlbum() {
        return album;
    }
}
