package pl.edu.wit.studentManagement.service.dto.studentGroup;

import pl.edu.wit.studentManagement.service.dto.student.StudentDto;

import java.util.List;
import java.util.UUID;

public class StudentGroupWithStudentsDto {
    private final UUID id;
    private final String code;
    private final String specialization;
    private final String description;
    private final List<StudentDto> students;

    public StudentGroupWithStudentsDto(UUID id, String code, String specialization, String description, List<StudentDto> students) {
        this.id = id;
        this.code = code;
        this.specialization = specialization;
        this.description = description;
        this.students = students;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getDescription() {
        return description;
    }

    public List<StudentDto> getStudents() {
        return students;
    }
}
