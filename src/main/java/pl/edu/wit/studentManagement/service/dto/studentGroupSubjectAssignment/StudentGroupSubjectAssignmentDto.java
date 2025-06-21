package pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing an existing assignment
 * between a student group and a subject.
 * <p>
 * Includes the unique ID of the assignment and references to
 * both the student group and the subject involved in the association.
 *
 * @author Michał Zawadzki
 */
public class StudentGroupSubjectAssignmentDto {
    private final UUID id;
    private final UUID studentGroupId;
    private final UUID subjectId;

    /**
     * Constructs a new {@code StudentGroupSubjectAssignmentDto}.
     *
     * @param id the unique identifier of the assignment
     * @param studentGroupId the ID of the assigned student group
     * @param subjectId the ID of the assigned subject
     */
    public StudentGroupSubjectAssignmentDto(UUID id, UUID studentGroupId, UUID subjectId) {
        this.id = id;
        this.studentGroupId = studentGroupId;
        this.subjectId = subjectId;
    }

    /**
     * Returns the ID of the assignment.
     *
     * @return the assignment ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the ID of the assigned student group.
     *
     * @return the student group ID
     */
    public UUID getStudentGroupId() {
        return studentGroupId;
    }

    /**
     * Returns the ID of the assigned subject.
     *
     * @return the subject ID
     */
    public UUID getSubjectId() {
        return subjectId;
    }
}
