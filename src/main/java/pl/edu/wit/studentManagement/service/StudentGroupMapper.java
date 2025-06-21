package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupWithStudentsDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting {@link StudentGroup} entities to their DTO representations.
 * <p>
 * Provides methods to map to simple DTOs and DTOs including associated students.
 * </p>
 *
 * @author Michał Zawadzki
 */
class StudentGroupMapper {

    /**
     * Maps a {@link StudentGroup} entity to a {@link StudentGroupDto}.
     *
     * @param studentGroup the entity to map
     * @return the corresponding DTO
     */
    static StudentGroupDto toDto(StudentGroup studentGroup) {
        return new StudentGroupDto(
                studentGroup.getId(),
                studentGroup.getCode(),
                studentGroup.getSpecialization(),
                studentGroup.getDescription()
        );
    }

    /**
     * Maps a {@link StudentGroup} entity and its associated {@link Student} entities
     * to a {@link StudentGroupWithStudentsDto}.
     *
     * @param studentGroup the student group entity
     * @param students the list of students belonging to the group
     * @return the corresponding DTO including students
     */
    static StudentGroupWithStudentsDto toWithStudentsDto(StudentGroup studentGroup, List<Student> students) {
        return new StudentGroupWithStudentsDto(
                studentGroup.getId(),
                studentGroup.getCode(),
                studentGroup.getSpecialization(),
                studentGroup.getDescription(),
                students.stream().map(StudentMapper::toDto).collect(Collectors.toList())
        );
    }
}
