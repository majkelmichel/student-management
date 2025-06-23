package pl.edu.wit.studentManagement.service.dto.studentGroup;

/**
 * Data Transfer Object used for updating student group details.
 * Contains mutable fields for partial updates.
 *
 * @author Michał Zawadzki
 */
public class UpdateStudentGroupDto {
    /**
     * Code representing the student group (e.g., "IZ06IO1")
     */
    private String code;
    /**
     * Specialization for this group
     */
    private String specialization;
    /**
     * Description of the student group
     */
    private String description;

    /**
     * Returns the student group's code.
     *
     * @return group code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the student group's code.
     *
     * @param code new group code
     */
    public void setCode(String code) {
        this.code = code;
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
     * Sets the student group's specialization.
     *
     * @param specialization new specialization
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * Returns the student group's description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the student group's description.
     *
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
