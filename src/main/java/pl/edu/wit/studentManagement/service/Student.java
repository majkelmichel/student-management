package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a student with personal details and group association.
 * <p>
 * Each Student is uniquely identified by a UUID and contains first name,
 * last name, album number, and optionally a reference to a student group by UUID.
 * <p>
 * The class supports validation of its fields.
 *
 * @author Michał Zawadzki
 */
class Student extends Entity {
    /**
     * Unique identifier of the student
     */
    private final UUID id;
    /**
     * First name of the student
     */
    private String firstName;
    /**
     * Last name of the student
     */
    private String lastName;
    /**
     * Album number of the student
     */
    private String album;
    /**
     * ID of the student group
     */
    private UUID studentGroupId;

    /**
     * Constructs a new Student with the given first name, last name, and album number.
     * The unique ID is automatically generated.
     *
     * @param firstName the student's first name
     * @param lastName  the student's last name
     * @param album     the student's album number (identifier)
     */
    Student(String firstName, String lastName, String album) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
    }

    Student(UUID id, String firstName, String lastName, String album, UUID studentGroupId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
        this.studentGroupId = studentGroupId;
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

    /**
     * Validates the student's fields to ensure data integrity.
     * <p>
     * Checks that the first name, last name, and album number are not null,
     * not empty, and meet basic formatting requirements.
     * <p>
     * The album number should be alphanumeric, typically between 5 and 10 characters,
     * for example: "A12345".
     * <p>
     * Throws {@link ValidationException} with specific messages if any validation fails.
     *
     * @throws ValidationException if any field is invalid or missing
     */
    void validate() throws ValidationException {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new ValidationException("student.firstName.empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new ValidationException("student.lastName.empty");
        }
        if (album == null || album.trim().isEmpty()) {
            throw new ValidationException("student.album.empty");
        }

        if (!album.matches("[A-Za-z0-9]{5,10}")) {
            throw new ValidationException("student.album.invalidFormat");
        }
    }
}
