package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupWithStudentsDto;

import java.util.List;
import java.util.stream.Collectors;

class StudentGroupMapper {
    static StudentGroupDto toDto(StudentGroup studentGroup) {
        return new StudentGroupDto(
                studentGroup.getId(),
                studentGroup.getCode(),
                studentGroup.getSpecialization(),
                studentGroup.getDescription()
        );
    }

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
