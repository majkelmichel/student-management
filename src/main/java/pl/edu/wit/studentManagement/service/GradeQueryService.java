package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixDto;
import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixRowDto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service providing query operations for retrieving structured grade information.
 * <p>
 * Specifically, this class generates a matrix view of grades for students in a given group
 * and subject, organized by grading criteria.
 * <p>
 * It aggregates data from the {@link Grade}, {@link Student}, and {@link GradeCriterion} entities.
 * This class is read-only and does not persist or modify data.
 *
 * @author Michał Zawadzki
 */
public class GradeQueryService {
    private final Dao<Grade> gradeDao;
    private final Dao<Student> studentDao;
    private final Dao<GradeCriterion> gradeCriterionDao;

    /**
     * Constructs a new instance of {@code GradeQueryService}.
     *
     * @param gradeDao          DAO for accessing grade records
     * @param studentDao        DAO for accessing student records
     * @param gradeCriterionDao DAO for accessing grading criteria
     */
    GradeQueryService(Dao<Grade> gradeDao, Dao<Student> studentDao, Dao<GradeCriterion> gradeCriterionDao) {
        this.gradeDao = gradeDao;
        this.studentDao = studentDao;
        this.gradeCriterionDao = gradeCriterionDao;
    }

    /**
     * Returns a grade matrix containing students from a given group and their grades
     * for a specified subject, organized by grade criteria.
     * <p>
     * The resulting {@link GradeMatrixDto} includes:
     * <ul>
     *   <li>Criterion names (as columns)</li>
     *   <li>A row per student, with their full name and corresponding grades</li>
     * </ul>
     *
     * @param subjectId the ID of the subject to query
     * @param groupId   the ID of the student group
     * @return a DTO representing the grade matrix
     */
    public GradeMatrixDto getGradeMatrixForSubjectAndGroup(UUID subjectId, UUID groupId) {
        var students = studentDao.getAll()
                .stream()
                .filter(s -> Objects.equals(s.getStudentGroupId(), groupId))
                .collect(Collectors.toList());

        var gradeCriteria = gradeCriterionDao.getAll()
                .stream()
                .filter(s -> Objects.equals(s.getSubjectId(), subjectId))
                .collect(Collectors.toList());

        var grades = gradeDao.getAll()
                .stream()
                .filter(s -> Objects.equals(s.getSubjectId(), subjectId))
                .collect(Collectors.toList());

        Map<UUID, Integer> criterionIndex = new HashMap<>();
        List<String> criterionNames = new ArrayList<>();
        for (int i = 0; i < gradeCriteria.size(); i++) {
            criterionIndex.put(gradeCriteria.get(i).getId(), i);
            criterionNames.add(gradeCriteria.get(i).getName());
        }

        List<GradeMatrixRowDto> rows = new ArrayList<>();
        for (Student student : students) {
            var gradesArray = new Byte[gradeCriteria.size()];
            Arrays.fill(gradesArray, null);

            for (Grade grade : grades) {
                if (grade.getStudentId().equals(student.getId())) {
                    var index = criterionIndex.get(grade.getGradeCriterionId());
                    if (index != null) {
                        gradesArray[index] = grade.getGrade();
                    }
                }
            }

            rows.add(new GradeMatrixRowDto(
                    student.getId(),
                    student.getFirstName() + " " + student.getLastName(),
                    Arrays.asList(gradesArray)
            ));
        }

        return new GradeMatrixDto(criterionNames, rows);
    }
}
