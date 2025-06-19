package pl.edu.wit.studentManagement.service;

public class ServiceFactory {
    private static StudentService studentService;
    private static StudentGroupService studentGroupService;
    private static SubjectService subjectService;
    private static GradeService gradeService;
    private static GradeQueryService gradeQueryService;

    // data stream handlers
    private static final StudentDataStreamHandler studentDataStreamHandler = new StudentDataStreamHandler("student.dat");
    private static final StudentGroupDataStreamHandler studentGroupDataStreamHandler = new StudentGroupDataStreamHandler("studentgroup.dat");
    private static final SubjectDataStreamHandler subjectDataStreamHandler = new SubjectDataStreamHandler("subject.dat");
    private static final GradeCriterionDataStreamHandler gradeCriterionDataStreamHandler = new GradeCriterionDataStreamHandler("gradecriterion.dat");
    private static final GradeDataStreamHandler gradeDataStreamHandler = new GradeDataStreamHandler("grade.dat");

    // DAOs
    private static final Dao<Student> studentDao = new StudentDao(studentDataStreamHandler);
    private static final Dao<StudentGroup> studentGroupDao = new StudentGroupDao(studentGroupDataStreamHandler);
    private static final Dao<Subject> subjectDao = new SubjectDao(subjectDataStreamHandler);
    private static final Dao<GradeCriterion> gradeCriterionDao = new GradeCriterionDao(gradeCriterionDataStreamHandler);
    private static final Dao<Grade> gradeDao = new GradeDao(gradeDataStreamHandler);

    public static StudentService getStudentService() {
        if (studentService == null) {
            studentService = new StudentService(studentDao, studentGroupDao);
        }

        return studentService;
    }

    public static StudentGroupService getStudentGroupService() {
        if (studentGroupService == null) {
            studentGroupService = new StudentGroupService(studentGroupDao, studentDao);
        }

        return studentGroupService;
    }

    public static SubjectService getSubjectService() {
        if (subjectService == null) {
            subjectService = new SubjectService(subjectDao, gradeCriterionDao, gradeDao);
        }

        return subjectService;
    }

    public static GradeService getGradeService() {
        if (gradeService == null) {
            gradeService = new GradeService(gradeDao, studentDao, gradeCriterionDao, subjectDao);
        }

        return gradeService;
    }

    public static GradeQueryService getGradeQueryService() {
        if (gradeQueryService == null) {
            gradeQueryService = new GradeQueryService(gradeDao, studentDao, gradeCriterionDao);
        }

        return gradeQueryService;
    }
}
