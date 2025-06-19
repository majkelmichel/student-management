package pl.edu.wit.studentManagement.service.dto.student;

public class CreateStudentDto {
    private final String firstName;
    private final String lastName;
    private final String album;

    public CreateStudentDto(String firstName, String lastName, String album) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAlbum() {
        return album;
    }
}
