package pl.edu.wit.studentManagement.service.dto.studentGroup;

import java.util.UUID;

public class StudentGroupDto {
    private final UUID id;
    private final String code;
    private final String specialization;
    private final String description;

    public StudentGroupDto(UUID id, String code, String specialization, String description) {
        this.id = id;
        this.code = code;
        this.specialization = specialization;
        this.description = description;
    }

    public UUID getId() {
        return id;
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
