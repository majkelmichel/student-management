package pl.edu.wit.studentManagement.service.dto.subject;

import java.util.UUID;

/**
 * Data Transfer Object representing a subject.
 * Immutable object containing the subject's ID and name.
 *
 * @author Michał Zawadzki
 */
public class SubjectDto {
    private final UUID id;
    private final String name;

    /**
     * Constructs a SubjectDto with the given ID and name.
     *
     * @param id unique identifier of the subject
     * @param name name of the subject
     */
    public SubjectDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the unique identifier of the subject.
     *
     * @return subject ID as UUID
     */
    public UUID getId() {
        return id;
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
