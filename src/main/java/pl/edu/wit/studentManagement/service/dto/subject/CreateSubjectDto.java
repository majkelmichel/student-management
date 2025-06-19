package pl.edu.wit.studentManagement.service.dto.subject;

public class CreateSubjectDto {
    private final String name;

    public CreateSubjectDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
