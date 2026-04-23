package com.projectpulse.backend.rubric.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RubricCriterionRequest {

    private String criterionName;
    private String description;
    private Integer maxScore;
}
