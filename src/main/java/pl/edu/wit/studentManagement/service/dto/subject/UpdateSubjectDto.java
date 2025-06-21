package pl.edu.wit.studentManagement.service.dto.subject;

/**
 * Data Transfer Object used for updating a subject.
 * Contains mutable field for the subject name.
 *
 * @author Michał Zawadzki
 */
public class UpdateSubjectDto {
    private String name;

    /**
     * Returns the name of the subject.
     *
     * @return subject name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the subject.
     *
     * @param name new subject name
     */
    public void setName(String name) {
        this.name = name;
    }
}
