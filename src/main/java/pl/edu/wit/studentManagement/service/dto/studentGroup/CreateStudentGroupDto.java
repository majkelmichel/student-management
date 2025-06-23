package pl.edu.wit.studentManagement.service.dto.studentGroup;

/**
 * Data Transfer Object used for creating a new student group.
 * Contains the basic information required to create the group.
 *
 * @author Michał Zawadzki
 */
public class CreateStudentGroupDto {
    /**
     * The unique code identifying the student group
     */
    private final String code;
    /**
     * The field of specialization for the student group
     */
    private final String specialization;
    /**
     * Additional information about the student group
     */
    private final String description;

    /**
     * Constructs a CreateStudentGroupDto with the given group details.
     *
     * @param code the code of the student group
     * @param specialization the specialization of the group
     * @param description description of the group
     */
    public CreateStudentGroupDto(String code, String specialization, String description) {
        this.code = code;
        this.specialization = specialization;
        this.description = description;
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
