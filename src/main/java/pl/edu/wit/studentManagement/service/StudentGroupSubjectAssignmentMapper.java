package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment.StudentGroupSubjectAssignmentDto;

/**
 * Provides mapping functionality between the {@link StudentGroupSubjectAssignment}
 * domain model and its corresponding Data Transfer Object (DTO).
 * <p>
 * Used to convert internal data structures into formats suitable for external communication.
 *
 * @author Michał Zawadzki
 */
class StudentGroupSubjectAssignmentMapper {

    /**
     * Converts a {@link StudentGroupSubjectAssignment} entity to a {@link StudentGroupSubjectAssignmentDto}.
     *
     * @param studentGroupSubjectAssignment the domain entity to convert
     * @return a DTO representation of the assignment
     */
    static StudentGroupSubjectAssignmentDto toDto(StudentGroupSubjectAssignment studentGroupSubjectAssignment) {
        return new StudentGroupSubjectAssignmentDto(
                studentGroupSubjectAssignment.getId(),
                studentGroupSubjectAssignment.getStudentGroupId(),
                studentGroupSubjectAssignment.getSubjectId()
        );
    }
}
