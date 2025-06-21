package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.gradeCriterion.CreateGradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.UpdateGradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.subject.CreateSubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectWithGradeCriteriaDto;
import pl.edu.wit.studentManagement.service.dto.subject.UpdateSubjectDto;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing {@link Subject} entities and their associated
 * {@link GradeCriterion} entities. Provides CRUD operations and business logic
 * for subjects and grade criteria.
 * <p>
 * Handles validation and integrity rules such as preventing deletion of subjects with assigned grade criteria,
 * and grade criteria with assigned grades.
 *
 * <p>
 * Interacts with DAOs to persist and retrieve data.
 *
 * @author Michał Zawadzki
 */
public class SubjectService {
    private final Dao<Subject> subjectDao;
    private final Dao<GradeCriterion> gradeCriterionDao;
    private final Dao<Grade> gradeDao;

    /**
     * Constructs the SubjectService with DAOs for subjects, grade criteria, and grades.
     *
     * @param subjectDao        DAO managing {@link Subject} persistence
     * @param gradeCriterionDao DAO managing {@link GradeCriterion} persistence
     * @param gradeDao          DAO managing {@link Grade} persistence
     */
    SubjectService(Dao<Subject> subjectDao, Dao<GradeCriterion> gradeCriterionDao, Dao<Grade> gradeDao) {
        this.subjectDao = subjectDao;
        this.gradeCriterionDao = gradeCriterionDao;
        this.gradeDao = gradeDao;
    }

    /**
     * Retrieves all subjects.
     *
     * @return list of all subjects as {@link SubjectDto}s
     */
    public List<SubjectDto> getAllSubjects() {
        return subjectDao.getAll().stream().map(SubjectMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a subject by its ID.
     *
     * @param id the UUID of the subject
     * @return an {@link Optional} containing the subject DTO if found, or empty if not
     */
    public Optional<SubjectDto> getSubjectById(UUID id) {
        var subject = subjectDao.get(id);
        return subject.map(SubjectMapper::toDto);
    }

    /**
     * Retrieves a subject by its ID along with its associated grade criteria.
     *
     * @param id the UUID of the subject
     * @return an {@link Optional} containing the subject with grade criteria DTO if found, or empty if not
     */
    public Optional<SubjectWithGradeCriteriaDto> getSubjectWithGradeCriteriaById(UUID id) {
        var subject = subjectDao.get(id);
        var gradeCriteria = gradeCriterionDao.getAll()
                .stream()
                .filter(g -> g.getSubjectId().equals(id))
                .collect(Collectors.toList());

        return subject.map(s -> SubjectMapper.toDtoWithGradeCriteria(s, gradeCriteria));
    }

    /**
     * Creates a new subject.
     *
     * @param createSubjectDto DTO containing subject creation data
     * @return the created subject as a DTO
     * @throws ValidationException if validation fails
     */
    public SubjectDto createSubject(CreateSubjectDto createSubjectDto) throws ValidationException {
        var newSubject = new Subject(
                createSubjectDto.getName()
        );

        subjectDao.save(newSubject);

        return SubjectMapper.toDto(newSubject);
    }

    /**
     * Updates an existing subject.
     *
     * @param id               UUID of the subject to update
     * @param updateSubjectDto DTO containing updated data
     * @return the updated subject as a DTO
     * @throws ValidationException if the subject does not exist or validation fails
     */
    public SubjectDto updateSubject(UUID id, UpdateSubjectDto updateSubjectDto) throws ValidationException {
        var subject = subjectDao.get(id).orElseThrow();

        if (updateSubjectDto.getName() != null) subject.setName(updateSubjectDto.getName());

        subjectDao.update(subject);

        return SubjectMapper.toDto(subject);
    }

    /**
     * Deletes a subject by its ID.
     * <p>
     * Prevents deletion if the subject has any associated grade criteria.
     *
     * @param id the UUID of the subject to delete
     * @return true if the subject was successfully deleted, false otherwise
     * @throws ValidationException if the subject has assigned grade criteria
     */
    public boolean deleteSubject(UUID id) throws ValidationException {
        var gradeCriteria = gradeCriterionDao.getAll()
                .stream()
                .filter(g -> g.getSubjectId().equals(id))
                .collect(Collectors.toList());

        if (!gradeCriteria.isEmpty()) throw new ValidationException("subject.delete.hasGradeCriteria");

        return subjectDao.delete(id);
    }

    /**
     * Adds a new grade criterion to a subject.
     *
     * @param subjectId          the UUID of the subject
     * @param createCriterionDto DTO containing data for the new grade criterion
     * @return the created grade criterion as a DTO
     * @throws ValidationException if the subject does not exist or validation fails
     */
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

    /**
     * Updates an existing grade criterion.
     *
     * @param id                    UUID of the grade criterion to update
     * @param updateGradeCriterionDto DTO containing updated data
     * @return the updated grade criterion as a DTO
     * @throws ValidationException if the grade criterion does not exist or validation fails
     */
    public GradeCriterionDto updateGradeCriterion(UUID id, UpdateGradeCriterionDto updateGradeCriterionDto) throws ValidationException {
        var gradeCriterion = gradeCriterionDao.get(id).orElseThrow(() -> new ValidationException("gradeCriterion.notExists"));

        if (updateGradeCriterionDto.getName() != null) gradeCriterion.setName(updateGradeCriterionDto.getName());
        if (updateGradeCriterionDto.getMaxPoints() != null) gradeCriterion.setMaxPoints(updateGradeCriterionDto.getMaxPoints());

        gradeCriterionDao.update(gradeCriterion);

        return GradeCriterionMapper.toDto(gradeCriterion);
    }

    /**
     * Deletes a grade criterion by its ID.
     * <p>
     * Prevents deletion if any grades are assigned to the criterion.
     *
     * @param id the UUID of the grade criterion to delete
     * @return true if the grade criterion was successfully deleted, false otherwise
     * @throws ValidationException if there are grades assigned to the criterion
     */
    public boolean deleteGradeCriterion(UUID id) throws ValidationException {
        var grades = gradeDao.getAll()
                .stream()
                .filter(g -> g.getGradeCriterionId().equals(id))
                .collect(Collectors.toList());

        if (!grades.isEmpty()) throw new ValidationException("gradeCriterion.delete.hasGrades");

        return gradeCriterionDao.delete(id);
    }
}
