package pl.edu.wit.studentManagement.service;

public class StudentGroupService {
    private final Dao<StudentGroup> studentGroupDao;

    StudentGroupService(Dao<StudentGroup> studentGroupDao) {
        this.studentGroupDao = studentGroupDao;
    }
}
