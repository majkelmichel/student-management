package pl.edu.wit.studentManagement.service.dto.studentGroup;

public class CreateStudentGroupDto {
    private final String code;
    private final String specialization;
    private final String description;

    public CreateStudentGroupDto(String code, String specialization, String description) {
        this.code = code;
        this.specialization = specialization;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getDescription() {
        return description;
    }
}
