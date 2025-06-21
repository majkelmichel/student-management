package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;

/**
 * Maps {@link GradeCriterion} entities to {@link GradeCriterionDto} objects.
 * <p>
 * This utility class is used to separate internal data models from external data transfer objects (DTOs).
 * It helps in maintaining a clear boundary between the service layer and any client-facing representations.
 * <p>
 * This class is not intended to be instantiated.
 *
 * @author Michał Zawadzki
 */
class GradeCriterionMapper {

    /**
     * Converts a {@link GradeCriterion} entity to its corresponding {@link GradeCriterionDto}.
     *
     * @param gradeCriterion the grade criterion entity to convert
     * @return the corresponding DTO
     */
    static GradeCriterionDto toDto(GradeCriterion gradeCriterion) {
        return new GradeCriterionDto(
                gradeCriterion.getId(),
                gradeCriterion.getName(),
                gradeCriterion.getMaxPoints()
        );
    }
}
