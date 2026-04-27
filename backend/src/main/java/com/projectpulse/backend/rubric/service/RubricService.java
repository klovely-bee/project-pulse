package com.projectpulse.backend.rubric.service;

import com.projectpulse.backend.rubric.domain.Rubric;
import com.projectpulse.backend.rubric.domain.RubricCriterion;
import com.projectpulse.backend.rubric.dto.CreateRubricRequest;
import com.projectpulse.backend.rubric.dto.RubricCriterionRequest;
import com.projectpulse.backend.rubric.dto.RubricResponse;
import com.projectpulse.backend.rubric.repository.RubricRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RubricService {

    private final RubricRepository rubricRepository;

    public RubricService(RubricRepository rubricRepository) {
        this.rubricRepository = rubricRepository;
    }

    public RubricResponse createRubric(CreateRubricRequest request) {
        Rubric rubric = Rubric.builder()
                .name(request.getName())
                .description(request.getDescription())
                .criteria(new ArrayList<>())
                .build();

        if (request.getCriteria() != null) {
            for (RubricCriterionRequest criterionRequest : request.getCriteria()) {
                RubricCriterion criterion = RubricCriterion.builder()
                        .criterionName(criterionRequest.getCriterionName())
                        .description(criterionRequest.getDescription())
                        .maxScore(criterionRequest.getMaxScore())
                        .rubric(rubric)
                        .build();
                rubric.getCriteria().add(criterion);
            }
        }

        Rubric savedRubric = rubricRepository.save(rubric);
        return toRubricResponse(savedRubric);
    }

    private RubricResponse toRubricResponse(Rubric rubric) {
        List<RubricCriterionRequest> criteria = rubric.getCriteria().stream()
                .map(this::toRubricCriterionRequest)
                .toList();

        return RubricResponse.builder()
                .id(rubric.getId())
                .name(rubric.getName())
                .description(rubric.getDescription())
                .criteria(criteria)
                .build();
    }

    private RubricCriterionRequest toRubricCriterionRequest(RubricCriterion criterion) {
        return RubricCriterionRequest.builder()
                .criterionName(criterion.getCriterionName())
                .description(criterion.getDescription())
                .maxScore(criterion.getMaxScore())
                .build();
    }
}
