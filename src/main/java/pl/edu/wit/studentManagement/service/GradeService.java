package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.grade.AssignGradeDto;
import pl.edu.wit.studentManagement.service.dto.grade.GradeDto;
import pl.edu.wit.studentManagement.service.dto.grade.UpdateGradeDto;
import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.UUID;

/**
 * Service layer for managing {@link Grade} operations such as assignment, update, and deletion.
 * <p>
 * This class acts as an application-level coordinator that ensures related entities
 * exist and that business rules are enforced (e.g. max grade validation).
 * It delegates persistence to the appropriate DAOs.
 *
 * <p>No data transformation or complex logic is handled here beyond validation and composition.
 *
 * @author Michał Zawadzki
 */
public class GradeService {
    private final Dao<Grade> gradeDao;
    private final Dao<Student> studentDao;
    private final Dao<GradeCriterion> gradeCriterionDao;
    private final Dao<Subject> subjectDao;

    /**
     * Constructs a new {@code GradeService} with the required DAOs.
     *
     * @param gradeDao            DAO for Grade entities
     * @param studentDao          DAO for Student entities
     * @param gradeCriterionDao   DAO for GradeCriterion entities
     * @param subjectDao          DAO for Subject entities
     */
    GradeService(Dao<Grade> gradeDao, Dao<Student> studentDao, Dao<GradeCriterion> gradeCriterionDao, Dao<Subject> subjectDao) {
        this.gradeDao = gradeDao;
        this.studentDao = studentDao;
        this.gradeCriterionDao = gradeCriterionDao;
        this.subjectDao = subjectDao;
    }

    /**
     * Assigns a new grade to a student for a specific subject and criterion.
     *
     * <p>This method verifies that the student, subject, and grade criterion exist.
     * It also checks that the grade value is within allowed bounds.
     *
     * @param assignGradeDto the DTO containing the assignment details
     * @return a {@link GradeDto} representing the newly assigned grade
     * @throws ValidationException if referenced entities do not exist or grade value is invalid
     */
    public GradeDto assignGrade(AssignGradeDto assignGradeDto) throws ValidationException {
        var student = studentDao.get(assignGradeDto.getStudentId())
                .orElseThrow(() -> new ValidationException("student.notExists"));
        var subject = subjectDao.get(assignGradeDto.getSubjectId())
                .orElseThrow(() -> new ValidationException("subject.notExists"));
        var gradeCriterion = gradeCriterionDao.get(assignGradeDto.getGradeCriterionId())
                .orElseThrow(() -> new ValidationException("gradeCriterion.notExists"));

        if (assignGradeDto.getGrade() < 0 || assignGradeDto.getGrade() > gradeCriterion.getMaxPoints()) {
            throw new ValidationException("grade.wrongGrade");
        }

        var newGrade = new Grade(subject.getId(), gradeCriterion.getId(), student.getId(), assignGradeDto.getGrade());
        gradeDao.save(newGrade);

        return GradeMapper.toDto(newGrade);
    }

    /**
     * Updates an existing grade's value.
     *
     * <p>This method does not allow modifying the associated student, subject, or criterion.
     * It only updates the numeric grade value.
     *
     * @param gradeId        the UUID of the grade to update
     * @param updateGradeDto the DTO containing the new grade value
     * @return a {@link GradeDto} reflecting the updated grade
     * @throws ValidationException if the grade does not exist or fails validation
     */
    public GradeDto updateGrade(UUID gradeId, UpdateGradeDto updateGradeDto) throws ValidationException {
        var grade = gradeDao.get(gradeId)
                .orElseThrow(() -> new ValidationException("grade.notExists"));

        if (updateGradeDto.getGrade() != null) {
            grade.setGrade(updateGradeDto.getGrade());
        }

        gradeDao.update(grade);
        return GradeMapper.toDto(grade);
    }

    /**
     * Deletes a grade by its identifier.
     *
     * @param gradeId the UUID of the grade to delete
     * @return {@code true} if the grade was successfully deleted, {@code false} otherwise
     * @throws ValidationException if the deletion process encounters validation issues
     */
    public boolean deleteGrade(UUID gradeId) throws ValidationException {
        return gradeDao.delete(gradeId);
    }
}
