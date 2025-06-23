package pl.edu.wit.studentManagement.service.dto.student;

/**
 * Data Transfer Object used for creating a new student.
 * Contains the necessary fields to create a student entity.
 *
 * @author Michał Zawadzki
 */
public class CreateStudentDto {
    /**
     * The student's first name.
     */
    private final String firstName;
    /**
     * The student's last name.
     */
    private final String lastName;
    /**
     * The student's album number (student ID).
     */
    private final String album;

    /**
     * Constructs a CreateStudentDto with the given student details.
     *
     * @param firstName the student's first name
     * @param lastName the student's last name
     * @param album the student's album number (student ID)
     */
    public CreateStudentDto(String firstName, String lastName, String album) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
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
