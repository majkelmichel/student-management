package pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for creating a new assignment between
 * a student group and a subject.
 * <p>
 * This DTO carries the identifiers of the student group and the subject
 * to be associated.
 *
 * @author Michał Zawadzki
 */
public class CreateStudentGroupSubjectAssignmentDto {
    private final UUID studentGroupId;
    private final UUID subjectId;

    /**
     * Constructs a new {@code CreateStudentGroupSubjectAssignmentDto}.
     *
     * @param studentGroupId the ID of the student group to assign
     * @param subjectId the ID of the subject to assign
     */
    public CreateStudentGroupSubjectAssignmentDto(UUID studentGroupId, UUID subjectId) {
        this.studentGroupId = studentGroupId;
        this.subjectId = subjectId;
    }

    /**
     * Returns the ID of the student group to be assigned.
     *
     * @return the student group ID
     */
    public UUID getStudentGroupId() {
        return studentGroupId;
    }

    /**
     * Returns the ID of the subject to be assigned.
     *
     * @return the subject ID
     */
    public UUID getSubjectId() {
        return subjectId;
    }
}
