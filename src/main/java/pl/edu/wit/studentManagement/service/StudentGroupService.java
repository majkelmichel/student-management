package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.studentGroup.CreateStudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupWithStudentsDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.UpdateStudentGroupDto;
import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StudentGroupService {
    private final Dao<StudentGroup> studentGroupDao;
    private final Dao<Student> studentDao;

    StudentGroupService(Dao<StudentGroup> studentGroupDao, Dao<Student> studentDao) {
        this.studentGroupDao = studentGroupDao;
        this.studentDao = studentDao;
    }

    public List<StudentGroupDto> getAll() {
        return studentGroupDao.getAll().stream().map(StudentGroupMapper::toDto).collect(Collectors.toList());
    }

    public StudentGroupWithStudentsDto getWithStudentsById(UUID id) {
        var studentGroup = studentGroupDao.get(id).orElseThrow();
        var students = studentDao.getAll()
                .stream()
                .filter(student -> student.getStudentGroupId().equals(studentGroup.getId()))
                .collect(Collectors.toList());

        return StudentGroupMapper.toWithStudentsDto(studentGroup, students);
    }

    public StudentGroupDto create(CreateStudentGroupDto studentGroupDto) throws ValidationException {
        var newGroup = new StudentGroup(
                studentGroupDto.getCode(),
                studentGroupDto.getSpecialization(),
                studentGroupDto.getDescription()
        );

        studentGroupDao.save(newGroup);

        return StudentGroupMapper.toDto(newGroup);
    }

    public StudentGroupDto update(UUID id, UpdateStudentGroupDto dto) throws ValidationException {
        var studentGroup = studentGroupDao.get(id).orElseThrow();

        if (dto.getCode() != null) studentGroup.setCode(studentGroup.getCode());
        if (dto.getDescription() != null) studentGroup.setDescription(studentGroup.getDescription());
        if (dto.getSpecialization() != null) studentGroup.setSpecialization(studentGroup.getSpecialization());

        studentGroupDao.update(studentGroup);

        return StudentGroupMapper.toDto(studentGroup);
    }

    public boolean delete(UUID id) throws ValidationException {
        var studentsInGroup = studentDao.getAll().stream().filter(student -> student.getStudentGroupId().equals(id)).collect(Collectors.toList());

        if (!studentsInGroup.isEmpty()) throw new ValidationException("studentGroup.delete.notEmpty");

        return studentGroupDao.delete(id);
    }
}
