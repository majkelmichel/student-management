package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.grade.AssignGradeDto;
import pl.edu.wit.studentManagement.service.dto.grade.GradeDto;
import pl.edu.wit.studentManagement.service.dto.grade.UpdateGradeDto;
import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.UUID;

public class GradeService {
    private final Dao<Grade> gradeDao;
    private final Dao<Student> studentDao;
    private final Dao<GradeCriterion> gradeCriterionDao;
    private final Dao<Subject> subjectDao;

    GradeService(Dao<Grade> gradeDao, Dao<Student> studentDao, Dao<GradeCriterion> gradeCriterionDao, Dao<Subject> subjectDao) {
        this.gradeDao = gradeDao;
        this.studentDao = studentDao;
        this.gradeCriterionDao = gradeCriterionDao;
        this.subjectDao = subjectDao;
    }

    public GradeDto assignGrade(AssignGradeDto assignGradeDto) throws ValidationException {
        var student = studentDao.get(assignGradeDto.getStudentId()).orElseThrow(() -> new ValidationException("student.notExists"));
        var subject = subjectDao.get(assignGradeDto.getSubjectId()).orElseThrow(() -> new ValidationException("subject.notExists"));
        var gradeCriterion = gradeCriterionDao.get(assignGradeDto.getGradeCriterionId()).orElseThrow(() -> new ValidationException("gradeCriterion.notExists"));

        if (assignGradeDto.getGrade() < 0 || assignGradeDto.getGrade() > gradeCriterion.getMaxPoints()) throw new ValidationException("grade.wrongGrade");

        var newGrade = new Grade(subject.getId(), gradeCriterion.getId(), student.getId(), assignGradeDto.getGrade());

        gradeDao.save(newGrade);

        return GradeMapper.toDto(newGrade);
    }

    public GradeDto updateGrade(UUID gradeId, UpdateGradeDto updateGradeDto) throws ValidationException {
        var grade = gradeDao.get(gradeId).orElseThrow(() -> new ValidationException("grade.notExists"));

        if (updateGradeDto.getGrade() != null) grade.setGrade(updateGradeDto.getGrade());

        gradeDao.update(grade);

        return GradeMapper.toDto(grade);
    }

    public boolean deleteGrade(UUID gradeId) throws ValidationException {
        return gradeDao.delete(gradeId);
    }
}
