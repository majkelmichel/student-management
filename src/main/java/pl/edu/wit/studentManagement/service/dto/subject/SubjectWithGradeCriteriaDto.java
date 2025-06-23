package pl.edu.wit.studentManagement.service.dto.subject;

import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object representing a subject along with its grade criteria.
 * Contains the subject's ID, name, and a list of associated grade criteria.
 *
 * @author Michał Zawadzki
 */
public class SubjectWithGradeCriteriaDto {
    /**
     * Unique identifier of the subject
     */
    private final UUID id;
    /**
     * Name of the subject
     */
    private final String name;
    /**
     * List of grade criteria for the subject
     */
    private final List<GradeCriterionDto> gradeCriteria;

    /**
     * Constructs a SubjectWithGradeCriteriaDto with the given information.
     *
     * @param id unique identifier of the subject
     * @param name name of the subject
     * @param gradeCriteria list of grade criteria related to this subject
     */
    public SubjectWithGradeCriteriaDto(UUID id, String name, List<GradeCriterionDto> gradeCriteria) {
        this.id = id;
        this.name = name;
        this.gradeCriteria = gradeCriteria;
    }

    /**
     * Returns the unique identifier of the subject.
     *
     * @return subject ID as UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the name of the subject.
     *
     * @return subject name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of grade criteria associated with this subject.
     *
     * @return list of GradeCriterionDto
     */
    public List<GradeCriterionDto> getGradeCriteria() {
        return gradeCriteria;
    }
}
