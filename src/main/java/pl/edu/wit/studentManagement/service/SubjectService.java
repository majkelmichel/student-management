package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.gradeCriterion.CreateGradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.UpdateGradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.subject.CreateSubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectWithGradeCriteriaDto;
import pl.edu.wit.studentManagement.service.dto.subject.UpdateSubjectDto;
import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubjectService {
    private final Dao<Subject> subjectDao;
    private final Dao<GradeCriterion> gradeCriterionDao;
    private final Dao<Grade> gradeDao;

    SubjectService(Dao<Subject> subjectDao, Dao<GradeCriterion> gradeCriterionDao, Dao<Grade> gradeDao) {
        this.subjectDao = subjectDao;
        this.gradeCriterionDao = gradeCriterionDao;
        this.gradeDao = gradeDao;
    }

    public List<SubjectDto> getAllSubjects() {
        return subjectDao.getAll().stream().map(SubjectMapper::toDto).collect(Collectors.toList());
    }

    public Optional<SubjectDto> getSubjectById(UUID id) {
        var subject = subjectDao.get(id);
        return subject.map(SubjectMapper::toDto);
    }

    public Optional<SubjectWithGradeCriteriaDto> getSubjectWithGradeCriteriaById(UUID id) {
        var subject = subjectDao.get(id);
        var gradeCriteria = gradeCriterionDao.getAll()
                .stream()
                .filter(g -> g.getSubjectId().equals(id))
                .collect(Collectors.toList());

        return subject.map(s -> SubjectMapper.toDtoWithGradeCriteria(s, gradeCriteria));
    }

    public SubjectDto createSubject(CreateSubjectDto createSubjectDto) throws ValidationException {
        var newSubject = new Subject(
                createSubjectDto.getName()
        );

        subjectDao.save(newSubject);

        return SubjectMapper.toDto(newSubject);
    }

    public SubjectDto updateSubject(UUID id, UpdateSubjectDto updateSubjectDto) throws ValidationException {
        var subject = subjectDao.get(id).orElseThrow();

        if (updateSubjectDto.getName() != null) subject.setName(updateSubjectDto.getName());

        subjectDao.update(subject);

        return SubjectMapper.toDto(subject);
    }

    public boolean deleteSubject(UUID id) throws ValidationException {
        var gradeCriteria = gradeCriterionDao.getAll()
                .stream()
                .filter(g -> g.getSubjectId().equals(id))
                .collect(Collectors.toList());

        if (!gradeCriteria.isEmpty()) throw new ValidationException("subject.delete.hasGradeCriteria");

        return subjectDao.delete(id);
    }

    public GradeCriterionDto addCriterionToSubject(UUID subjectId, CreateGradeCriterionDto createCriterionDto) throws ValidationException {
        var subject = subjectDao.get(subjectId).orElseThrow(() -> new ValidationException("subject.notExists"));

        var newGradeCriterion = new GradeCriterion(
                createCriterionDto.getName(),
                createCriterionDto.getMaxPoints(),
                subject.getId()
        );

        gradeCriterionDao.save(newGradeCriterion);

        return GradeCriterionMapper.toDto(newGradeCriterion);
    }

    public GradeCriterionDto updateGradeCriterion(UUID id, UpdateGradeCriterionDto updateGradeCriterionDto) throws ValidationException {
        var gradeCriterion = gradeCriterionDao.get(id).orElseThrow(() -> new ValidationException("gradeCriterion.notExists"));

        if (updateGradeCriterionDto.getName() != null) gradeCriterion.setName(updateGradeCriterionDto.getName());
        if (updateGradeCriterionDto.getMaxPoints() != null) gradeCriterion.setMaxPoints(updateGradeCriterionDto.getMaxPoints());

        gradeCriterionDao.update(gradeCriterion);

        return GradeCriterionMapper.toDto(gradeCriterion);
    }

    public boolean deleteGradeCriterion(UUID id) throws ValidationException {
        var grades = gradeDao.getAll()
                .stream()
                .filter(g -> g.getGradeCriterionId().equals(id))
                .collect(Collectors.toList());

        if (!grades.isEmpty()) throw new ValidationException("gradeCriterion.delete.hasGrades");

        return gradeCriterionDao.delete(id);
    }
}
