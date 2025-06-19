package pl.edu.wit.studentManagement.service;

public class ServiceFactory {
    private static StudentService studentService;
    private static StudentGroupService studentGroupService;
    private static SubjectService subjectService;

    public static StudentService getStudentService() {
        // TODO: add factory or something else for DAO
        if (studentService == null) {
            var studentDataStreamHandler = new StudentDataStreamHandler("student.dat");
            var studentDao = new StudentDao(studentDataStreamHandler);
            var studentGroupDataStreamHandler = new StudentGroupDataStreamHandler("studentgroup.dat");
            var studentGroupDao = new StudentGroupDao(studentGroupDataStreamHandler);
            studentService = new StudentService(studentDao, studentGroupDao);
        }

        return studentService;
    }

    public static StudentGroupService getStudentGroupService() {
        if (studentGroupService == null) {
            var studentGroupDataStreamHandler = new StudentGroupDataStreamHandler("studentgroup.dat");
            var studentGroupDao = new StudentGroupDao(studentGroupDataStreamHandler);
            studentGroupService = new StudentGroupService(studentGroupDao);
        }

        return studentGroupService;
    }

    public static SubjectService getSubjectService() {
        if (subjectService == null) {
            var subjectDataStreamHandler = new SubjectDataStreamHandler("subject.dat");
            var subjectDao = new SubjectDao(subjectDataStreamHandler);
            subjectService = new SubjectService(subjectDao);
        }

        return subjectService;
    }
}
