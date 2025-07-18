package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.grade.AssignGradeDto;
import pl.edu.wit.studentManagement.service.dto.grade.GradeDto;
import pl.edu.wit.studentManagement.exceptions.ValidationException;

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
    /**
     * Data access object for managing Grade entities
     */
    private final Dao<Grade> gradeDao;
    /**
     * Data access object for managing Student entities
     */
    private final Dao<Student> studentDao;
    /**
     * Data access object for managing GradeCriterion entities
     */
    private final Dao<GradeCriterion> gradeCriterionDao;
    /**
     * Data access object for managing Subject entities
     */
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

        // check if the grade is being updated or a new one has to be added
        var grade = gradeDao.getAll()
                .stream()
                .filter(g -> g.getStudentId().equals(assignGradeDto.getStudentId()) &&
                        g.getGradeCriterionId().equals(assignGradeDto.getGradeCriterionId()))
                .findFirst();

        if (grade.isPresent()) {
            // update existing grade
            grade.get().setGrade(assignGradeDto.getGrade());
            gradeDao.update(grade.get());
            return GradeMapper.toDto(grade.get());
        }

        var newGrade = new Grade(subject.getId(), gradeCriterion.getId(), student.getId(), assignGradeDto.getGrade());
        gradeDao.save(newGrade);

        return GradeMapper.toDto(newGrade);
    }

    /**
     * Deletes a grade by its identifier.
     *
     * @param gradeId the UUID of the grade to delete
     * @return {@code true} if the grade was successfully deleted, {@code false} otherwise
     */
    public boolean deleteGrade(UUID gradeId) {
        return gradeDao.delete(gradeId);
    }
}
