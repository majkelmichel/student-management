package pl.edu.wit.studentManagement.service.dto.subject;

/**
 * Data Transfer Object used for creating a new subject.
 * Contains only the subject name.
 *
 * @author Michał Zawadzki
 */
public class CreateSubjectDto {
    private final String name;

    /**
     * Constructs a CreateSubjectDto with the given subject name.
     *
     * @param name the name of the subject
     */
    public CreateSubjectDto(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the subject.
     *
     * @return subject name
     */
    public String getName() {
        return name;
    }
}
