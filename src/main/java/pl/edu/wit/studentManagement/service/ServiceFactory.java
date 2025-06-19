package pl.edu.wit.studentManagement.service;

public class ServiceFactory {
    private static StudentService studentService;
    private static StudentGroupService studentGroupService;
    private static SubjectService subjectService;

    // data stream handlers
    private static final StudentDataStreamHandler studentDataStreamHandler = new StudentDataStreamHandler("student.dat");
    private static final StudentGroupDataStreamHandler studentGroupDataStreamHandler = new StudentGroupDataStreamHandler("studentgroup.dat");
    private static final SubjectDataStreamHandler subjectDataStreamHandler = new SubjectDataStreamHandler("subject.dat");

    // DAOs
    private static final Dao<Student> studentDao = new StudentDao(studentDataStreamHandler);
    private static final Dao<StudentGroup> studentGroupDao = new StudentGroupDao(studentGroupDataStreamHandler);
    private static final Dao<Subject> subjectDao = new SubjectDao(subjectDataStreamHandler);

    public static StudentService getStudentService() {
        // TODO: add factory or something else for DAO
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
            subjectService = new SubjectService(subjectDao);
        }

        return subjectService;
    }
}
