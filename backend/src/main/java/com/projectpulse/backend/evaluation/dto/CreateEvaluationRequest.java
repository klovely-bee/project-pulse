package com.projectpulse.backend.evaluation.dto;

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
public class CreateEvaluationRequest {

    private Long evaluatorId;
    private Long evaluateeId;
    private Long teamId;
    private Integer weekNumber;
    private Integer score;
    private String comments;
}
