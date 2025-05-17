package pl.edu.wit.studentManagement.entities;

import pl.edu.wit.studentManagement.entities.fields.StudentField;
import pl.edu.wit.studentManagement.validation.ValidationError;
import pl.edu.wit.studentManagement.validation.ValidationErrorType;
import pl.edu.wit.studentManagement.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Czy student może być w wielu grupach?
 * @author Michał Zawadzki
 */
public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String album;

    public Student(int id, String firstName, String lastName, String album) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void validate() throws ValidationException {
        // TODO: sprawdzić, czy w ogóle potrzebna jest walidacja
        List<ValidationError> errors = new ArrayList<>();

        // TODO: add all validation logic
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add(new ValidationError(StudentField.FIRST_NAME, ValidationErrorType.EMPTY));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
