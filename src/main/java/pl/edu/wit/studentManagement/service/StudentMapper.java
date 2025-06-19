package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.student.StudentDto;

class StudentMapper {
    static StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getAlbum()
        );
    }

    static Student fromDto(StudentDto studentDto) {
        return new Student(
                studentDto.getId(),
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getAlbum()
        );
    }
}
