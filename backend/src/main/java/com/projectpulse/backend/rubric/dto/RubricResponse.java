package com.projectpulse.backend.rubric.dto;

import java.util.List;
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
public class RubricResponse {

    private Long id;
    private String name;
    private String description;
    private List<RubricCriterionRequest> criteria;
}
