package pl.edu.wit.studentManagement.service.dto.subject;

import java.util.UUID;

public class SubjectDto {
    private final UUID id;
    private final String name;

    public SubjectDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
