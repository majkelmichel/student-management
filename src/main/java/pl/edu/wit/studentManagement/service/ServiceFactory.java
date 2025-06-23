package pl.edu.wit.studentManagement.service;

/**
 * Factory class responsible for providing singleton service instances.
 * <p>
 * Initializes and provides access to core services within the application,
 * including students, student groups, subjects, grades, and grade queries.
 * <p>
 * Underlying DAOs and data stream handlers are constructed once and reused.
 * Services are lazily initialized upon first access.
 * <p>
 * This factory ensures a single source of truth and consistent dependencies across the domain layer.
 *
 * <p>Data is persisted using stream-based handlers backed by files.
 *
 * @author Michał Zawadzki
 */
public class ServiceFactory {
    private static StudentService studentService;
    private static StudentGroupService studentGroupService;
    private static SubjectService subjectService;
    private static GradeService gradeService;
    private static GradeQueryService gradeQueryService;
    private static StudentGroupSubjectAssignmentService studentGroupSubjectAssignmentService;

    // Data stream handlers
    private static final StudentDataStreamHandler studentDataStreamHandler =
            new StudentDataStreamHandler("student.dat");
    private static final StudentGroupDataStreamHandler studentGroupDataStreamHandler =
            new StudentGroupDataStreamHandler("studentgroup.dat");
    private static final SubjectDataStreamHandler subjectDataStreamHandler =
            new SubjectDataStreamHandler("subject.dat");
    private static final GradeCriterionDataStreamHandler gradeCriterionDataStreamHandler =
            new GradeCriterionDataStreamHandler("gradecriterion.dat");
    private static final GradeDataStreamHandler gradeDataStreamHandler =
            new GradeDataStreamHandler("grade.dat");
    private static final StudentGroupSubjectAssignmentDataStreamHandler studentGroupSubjectAssignmentDataStreamHandler =
            new StudentGroupSubjectAssignmentDataStreamHandler("studentgroupsubject.dat");

    // DAOs
    private static final Dao<Student> studentDao = new Dao<>(studentDataStreamHandler);
    private static final Dao<StudentGroup> studentGroupDao = new Dao<>(studentGroupDataStreamHandler);
    private static final Dao<Subject> subjectDao = new Dao<>(subjectDataStreamHandler);
    private static final Dao<GradeCriterion> gradeCriterionDao = new Dao<>(gradeCriterionDataStreamHandler);
    private static final Dao<Grade> gradeDao = new Dao<>(gradeDataStreamHandler);
    private static final Dao<StudentGroupSubjectAssignment> studentGroupSubjectAssignmentDao =
            new Dao<>(studentGroupSubjectAssignmentDataStreamHandler);

    /**
     * Returns a singleton instance of {@link StudentService}.
     *
     * @return student service
     */
    public static StudentService getStudentService() {
        if (studentService == null) {
            studentService = new StudentService(studentDao, studentGroupDao, gradeDao);
        }
        return studentService;
    }

    /**
     * Returns a singleton instance of {@link StudentGroupService}.
     *
     * @return student group service
     */
    public static StudentGroupService getStudentGroupService() {
        if (studentGroupService == null) {
            studentGroupService = new StudentGroupService(studentGroupDao, studentDao);
        }
        return studentGroupService;
    }

    /**
     * Returns a singleton instance of {@link SubjectService}.
     *
     * @return subject service
     */
    public static SubjectService getSubjectService() {
        if (subjectService == null) {
            subjectService = new SubjectService(subjectDao, gradeCriterionDao, gradeDao);
        }
        return subjectService;
    }

    /**
     * Returns a singleton instance of {@link GradeService}.
     *
     * @return grade service
     */
    public static GradeService getGradeService() {
        if (gradeService == null) {
            gradeService = new GradeService(gradeDao, studentDao, gradeCriterionDao, subjectDao);
        }
        return gradeService;
    }

    /**
     * Returns a singleton instance of {@link GradeQueryService}.
     *
     * @return grade query service
     */
    public static GradeQueryService getGradeQueryService() {
        if (gradeQueryService == null) {
            gradeQueryService = new GradeQueryService(gradeDao, studentDao, gradeCriterionDao);
        }
        return gradeQueryService;
    }

    /**
     * Returns a singleton instance of {@link StudentGroupSubjectAssignmentService}.
     *
     * @return student group to subject assignment service
     */
    public static StudentGroupSubjectAssignmentService getStudentGroupSubjectAssignmentService() {
        if (studentGroupSubjectAssignmentService == null) {
            studentGroupSubjectAssignmentService = new StudentGroupSubjectAssignmentService(studentGroupSubjectAssignmentDao);
        }
        return studentGroupSubjectAssignmentService;
    }
}
