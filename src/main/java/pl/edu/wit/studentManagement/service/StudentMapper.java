package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.student.StudentDto;

/**
 * Mapper class for converting between {@link Student} entities and {@link StudentDto} data transfer objects.
 * <p>
 * Provides static methods to facilitate transformation to and from DTOs,
 * allowing separation between internal domain models and external data representations.
 *
 * @author Michał Zawadzki
 */
class StudentMapper {

    /**
     * Converts a {@link Student} entity to its corresponding {@link StudentDto}.
     *
     * @param student the {@code Student} entity to convert
     * @return the corresponding {@code StudentDto}
     */
    static StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getAlbum()
        );
    }
}
