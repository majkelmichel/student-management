package pl.edu.wit.studentManagement.service.dto.grade;

/**
 * Data Transfer Object used for updating the grade value.
 * <p>
 * Contains the optional grade value to update.
 *
 * @author Michał Zawadzki
 */
public class UpdateGradeDto {
    private Byte grade;

    public Byte getGrade() {
        return grade;
    }

    public void setGrade(Byte grade) {
        this.grade = grade;
    }
}
