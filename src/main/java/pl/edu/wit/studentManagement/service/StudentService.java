package pl.edu.wit.studentManagement.service;


public class StudentService {
    private final Dao<Student> studentDao;

    StudentService(Dao<Student> studentDao) {
        this.studentDao = studentDao;
    }
}
