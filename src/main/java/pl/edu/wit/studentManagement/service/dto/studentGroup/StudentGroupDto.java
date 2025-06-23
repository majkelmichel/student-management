package pl.edu.wit.studentManagement.service.dto.studentGroup;

import java.util.UUID;

/**
 * Data Transfer Object representing a student group.
 * Immutable representation with group details including its unique identifier.
 *
 * @author Michał Zawadzki
 */
public class StudentGroupDto {
    /**
     * Unique identifier of the student group
     */
    private final UUID id;
    /**
     * Code representing the student group (e.g., "IZ06IO1")
     */
    private final String code;
    /**
     * Specialization for this group
     */
    private final String specialization;
    /**
     * Description of the student group
     */
    private final String description;

    /**
     * Constructs a StudentGroupDto with all group information.
     *
     * @param id unique identifier of the group
     * @param code code of the group
     * @param specialization specialization of the group
     * @param description description of the group
     */
    public StudentGroupDto(UUID id, String code, String specialization, String description) {
        this.id = id;
        this.code = code;
        this.specialization = specialization;
        this.description = description;
    }

    /**
     * Returns the unique identifier of the student group.
     *
     * @return group ID as UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the student group's code.
     *
     * @return group code
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the student group's specialization.
     *
     * @return specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Returns the student group's description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }
}
