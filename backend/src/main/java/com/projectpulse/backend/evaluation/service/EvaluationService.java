package com.projectpulse.backend.evaluation.service;

import com.projectpulse.backend.evaluation.domain.Evaluation;
import com.projectpulse.backend.evaluation.dto.CreateEvaluationRequest;
import com.projectpulse.backend.evaluation.dto.EvaluationResponse;
import com.projectpulse.backend.evaluation.repository.EvaluationRepository;
import com.projectpulse.backend.team.domain.Team;
import com.projectpulse.backend.team.repository.TeamRepository;
import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.user.domain.User;
import com.projectpulse.backend.user.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public EvaluationService(EvaluationRepository evaluationRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public EvaluationResponse createEvaluation(CreateEvaluationRequest request) {
        User evaluator = userRepository.findById(request.getEvaluatorId())
                .orElseThrow(() -> new RuntimeException("Evaluator not found with id: " + request.getEvaluatorId()));
        User evaluatee = userRepository.findById(request.getEvaluateeId())
                .orElseThrow(() -> new RuntimeException("Evaluatee not found with id: " + request.getEvaluateeId()));
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + request.getTeamId()));

        if (evaluator.getRole() != Role.STUDENT || evaluatee.getRole() != Role.STUDENT) {
            throw new RuntimeException("Peer evaluations can only be submitted between students");
        }

        if (evaluator.getId().equals(evaluatee.getId())) {
            throw new RuntimeException("Students cannot evaluate themselves");
        }

        if (request.getScore() == null || request.getScore() < 1 || request.getScore() > 10) {
            throw new RuntimeException("Score must be between 1 and 10");
        }

        boolean evaluatorOnTeam = team.getStudents().stream().anyMatch(student -> student.getId().equals(evaluator.getId()));
        boolean evaluateeOnTeam = team.getStudents().stream().anyMatch(student -> student.getId().equals(evaluatee.getId()));
        if (!evaluatorOnTeam || !evaluateeOnTeam) {
            throw new RuntimeException("Both students must belong to the specified team");
        }

        validateWeekForTeam(team, request.getWeekNumber());

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

    public List<EvaluationResponse> getEvaluationsBySection(Long sectionId) {
        List<Long> teamIds = teamRepository.findBySectionId(sectionId).stream()
                .map(Team::getId)
                .toList();

        if (teamIds.isEmpty()) {
            return List.of();
        }

        return evaluationRepository.findByTeamIdInOrderByCreatedAtDesc(teamIds).stream()
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

    private void validateWeekForTeam(Team team, Integer weekNumber) {
        if (weekNumber == null || weekNumber < 1) {
            throw new RuntimeException("Week number must be at least 1");
        }

        if (team.getSection() == null || team.getSection().getActiveWeeks().isEmpty()) {
            return;
        }

        boolean matchesActiveWeek = team.getSection().getActiveWeeks().stream()
                .anyMatch(week -> week.getWeekNumber() != null && week.getWeekNumber().equals(weekNumber));

        if (!matchesActiveWeek) {
            throw new RuntimeException("Week number is not active for the team's section");
        }
    }
}
