package pl.edu.wit.studentManagement.service.dto.studentGroup;

import pl.edu.wit.studentManagement.service.dto.student.StudentDto;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object representing a student group including
 * the list of students belonging to this group.
 *
 * @author Michał Zawadzki
 */
public class StudentGroupWithStudentsDto {
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
     * List of students belonging to this group
     */
    private final List<StudentDto> students;

    /**
     * Constructs a StudentGroupWithStudentsDto with all group information and its students.
     *
     * @param id unique identifier of the group
     * @param code code of the group
     * @param specialization specialization of the group
     * @param description description of the group
     * @param students list of students in the group
     */
    public StudentGroupWithStudentsDto(UUID id, String code, String specialization, String description, List<StudentDto> students) {
        this.id = id;
        this.code = code;
        this.specialization = specialization;
        this.description = description;
        this.students = students;
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

    /**
     * Returns the list of students belonging to this group.
     *
     * @return list of StudentDto
     */
    public List<StudentDto> getStudents() {
        return students;
    }
}
