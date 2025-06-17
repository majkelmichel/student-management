package pl.edu.wit.studentManagement.service;

public class SubjectService {
    private final Dao<Subject> subjectDao;

    SubjectService(Dao<Subject> subjectDao) {
        this.subjectDao = subjectDao;
    }
}
