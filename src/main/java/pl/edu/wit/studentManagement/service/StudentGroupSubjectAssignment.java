package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.validation.ValidationException;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents an assignment of a Subject to a StudentGroup.
 * Manages the association between student groups and subjects by their IDs.
 *
 * @author Michał Zawadzki
 */
class StudentGroupSubjectAssignment implements Serializable {
    private final UUID id;
    private final UUID studentGroupId;
    private final UUID subjectId;

    /**
     * Constructs a new assignment between a student group and a subject.
     *
     * @param studentGroupId the ID of the student group
     * @param subjectId      the ID of the subject
     */
    StudentGroupSubjectAssignment(UUID studentGroupId, UUID subjectId) {
        this.id = UUID.randomUUID();
        this.studentGroupId = studentGroupId;
        this.subjectId = subjectId;
    }

    UUID getId() {
        return id;
    }

    UUID getStudentGroupId() {
        return studentGroupId;
    }

    UUID getSubjectId() {
        return subjectId;
    }

    /**
     * Validates the assignment.
     *
     * @throws ValidationException if any required field is missing
     */
    void validate() throws ValidationException {
        if (studentGroupId == null) {
            throw new ValidationException("studentGroupSubjectAssignment.studentGroupId.null");
        }
        if (subjectId == null) {
            throw new ValidationException("studentGroupSubjectAssignment.subjectId.null");
        }
    }
}
