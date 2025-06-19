package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixDto;
import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixRowDto;

import java.util.*;
import java.util.stream.Collectors;

public class GradeQueryService {
    private final Dao<Grade> gradeDao;
    private final Dao<Student> studentDao;
    private final Dao<GradeCriterion> gradeCriterionDao;

    GradeQueryService(Dao<Grade> gradeDao, Dao<Student> studentDao, Dao<GradeCriterion> gradeCriterionDao) {
        this.gradeDao = gradeDao;
        this.studentDao = studentDao;
        this.gradeCriterionDao = gradeCriterionDao;
    }

    public GradeMatrixDto getGradeMatrixForSubjectAndGroup(UUID subjectId, UUID groupId) {
        var students = studentDao.getAll()
                .stream()
                .filter(s -> s.getStudentGroupId().equals(groupId))
                .collect(Collectors.toList());
        var gradeCriteria = gradeCriterionDao.getAll()
                .stream()
                .filter(s -> s.getSubjectId().equals(subjectId))
                .collect(Collectors.toList());
        var grades = gradeDao.getAll()
                .stream()
                .filter(s -> s.getSubjectId().equals(subjectId))
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
                    List.of(gradesArray)
            ));
        }

        return new GradeMatrixDto(criterionNames, rows);
    }
}
