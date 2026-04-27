package com.projectpulse.backend.evaluation.service;

import com.projectpulse.backend.evaluation.domain.Evaluation;
import com.projectpulse.backend.evaluation.dto.CreateEvaluationRequest;
import com.projectpulse.backend.evaluation.dto.EvaluationResponse;
import com.projectpulse.backend.evaluation.repository.EvaluationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    public EvaluationResponse createEvaluation(CreateEvaluationRequest request) {
        Evaluation evaluation = Evaluation.builder()
                .evaluatorId(request.getEvaluatorId())
                .evaluateeId(request.getEvaluateeId())
                .teamId(request.getTeamId())
                .weekNumber(request.getWeekNumber())
                .score(request.getScore())
                .comments(request.getComments())
                .build();

        return toEvaluationResponse(evaluationRepository.save(evaluation));
    }

    public List<EvaluationResponse> getEvaluationsByStudent(Long evaluatorId) {
        return evaluationRepository.findByEvaluatorIdOrderByCreatedAtDesc(evaluatorId).stream()
                .map(this::toEvaluationResponse)
                .toList();
    }

    public List<EvaluationResponse> getEvaluationsByTeam(Long teamId) {
        return evaluationRepository.findByTeamIdOrderByCreatedAtDesc(teamId).stream()
                .map(this::toEvaluationResponse)
                .toList();
    }

    public List<EvaluationResponse> getEvaluationsForStudent(Long evaluateeId) {
        return evaluationRepository.findByEvaluateeIdOrderByCreatedAtDesc(evaluateeId).stream()
                .map(this::toEvaluationResponse)
                .toList();
    }

    private EvaluationResponse toEvaluationResponse(Evaluation evaluation) {
        return EvaluationResponse.builder()
                .id(evaluation.getId())
                .evaluatorId(evaluation.getEvaluatorId())
                .evaluateeId(evaluation.getEvaluateeId())
                .teamId(evaluation.getTeamId())
                .weekNumber(evaluation.getWeekNumber())
                .score(evaluation.getScore())
                .comments(evaluation.getComments())
                .createdAt(evaluation.getCreatedAt())
                .build();
    }
}
