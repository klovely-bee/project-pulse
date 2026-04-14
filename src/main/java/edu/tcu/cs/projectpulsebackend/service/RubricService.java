package edu.tcu.cs.projectpulsebackend.service;

import edu.tcu.cs.projectpulsebackend.dto.CriterionRequest;
import edu.tcu.cs.projectpulsebackend.dto.CriterionResponse;
import edu.tcu.cs.projectpulsebackend.dto.RubricCreateRequest;
import edu.tcu.cs.projectpulsebackend.dto.RubricResponse;
import edu.tcu.cs.projectpulsebackend.model.Criterion;
import edu.tcu.cs.projectpulsebackend.model.Rubric;
import edu.tcu.cs.projectpulsebackend.repository.RubricRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RubricService {

    private final RubricRepository rubricRepository;

    public RubricService(RubricRepository rubricRepository) {
        this.rubricRepository = rubricRepository;
    }

    @Transactional
    public RubricResponse createRubric(RubricCreateRequest request) {
        if (request == null) {
            throw new RuntimeException("Rubric request is required.");
        }
        if (isBlank(request.getName())) {
            throw new RuntimeException("Rubric name is required.");
        }
        String rubricName = request.getName().trim();
        if (rubricRepository.existsByName(rubricName)) {
            throw new RuntimeException("A rubric with this name already exists.");
        }
        if (request.getCriteria() == null || request.getCriteria().isEmpty()) {
            throw new RuntimeException("A rubric must have at least one criterion.");
        }

        Rubric rubric = new Rubric(rubricName);
        for (CriterionRequest criterionRequest : request.getCriteria()) {
            validateCriterion(criterionRequest);
            Criterion criterion = new Criterion(
                    criterionRequest.getName().trim(),
                    criterionRequest.getDescription().trim(),
                    criterionRequest.getMaxScore()
            );
            rubric.addCriterion(criterion);
        }

        Rubric savedRubric = rubricRepository.save(rubric);
        return mapToResponse(savedRubric);
    }

    @Transactional(readOnly = true)
    public List<RubricResponse> getAllRubrics() {
        return rubricRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RubricResponse getRubricById(Long id) {
        Rubric rubric = rubricRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rubric not found with id: " + id));
        return mapToResponse(rubric);
    }

    private void validateCriterion(CriterionRequest criterionRequest) {
        if (criterionRequest == null) {
            throw new RuntimeException("Criterion is required.");
        }
        if (isBlank(criterionRequest.getName())) {
            throw new RuntimeException("Criterion name must not be blank.");
        }
        if (isBlank(criterionRequest.getDescription())) {
            throw new RuntimeException("Criterion description must not be blank.");
        }
        if (criterionRequest.getMaxScore() == null || criterionRequest.getMaxScore() <= 0) {
            throw new RuntimeException("Criterion maxScore must be greater than 0.");
        }
    }

    private RubricResponse mapToResponse(Rubric rubric) {
        List<CriterionResponse> criteria = rubric.getCriteria().stream()
                .map(criterion -> new CriterionResponse(
                        criterion.getId(),
                        criterion.getName(),
                        criterion.getDescription(),
                        criterion.getMaxScore()
                ))
                .toList();

        return new RubricResponse(rubric.getId(), rubric.getName(), criteria);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
