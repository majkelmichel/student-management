package pl.edu.wit.studentManagement.service.dto.student;

/**
 * Data Transfer Object used for updating student details.
 * Fields can be modified through setters to allow partial updates.
 *
 * @author Michał Zawadzki
 */
public class UpdateStudentDto {
    private String firstName;
    private String lastName;
    private String album;

    /**
     * Returns the student's first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the student's first name.
     *
     * @param firstName new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
     * Sets the student's last name.
     *
     * @param lastName new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the student's album number.
     *
     * @return album number
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Sets the student's album number.
     *
     * @param album new album number
     */
    public void setAlbum(String album) {
        this.album = album;
    }
}
