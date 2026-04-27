package com.projectpulse.backend.evaluation.controller;

import com.projectpulse.backend.auth.service.SessionUserService;
import com.projectpulse.backend.evaluation.dto.CreateEvaluationRequest;
import com.projectpulse.backend.evaluation.dto.EvaluationResponse;
import com.projectpulse.backend.evaluation.service.EvaluationService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.user.domain.Role;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final SessionUserService sessionUserService;

    public EvaluationController(EvaluationService evaluationService, SessionUserService sessionUserService) {
        this.evaluationService = evaluationService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping
    public ApiResponse<EvaluationResponse> createEvaluation(@RequestBody CreateEvaluationRequest request, HttpSession session) {
        var user = sessionUserService.requireRole(session, Role.STUDENT);
        if (!user.getId().equals(request.getEvaluatorId())) {
            throw new RuntimeException("Forbidden");
        }
        return Result.success("Evaluation created successfully", evaluationService.createEvaluation(request));
    }

    @GetMapping("/student/{id}")
    public ApiResponse<List<EvaluationResponse>> getEvaluationsByStudent(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireSelfOrRole(session, id, Role.ADMIN, Role.INSTRUCTOR);
        return Result.success("Student evaluations retrieved successfully", evaluationService.getEvaluationsByStudent(id));
    }

    @GetMapping("/team/{teamId}")
    public ApiResponse<List<EvaluationResponse>> getEvaluationsByTeam(@PathVariable Long teamId, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN, Role.INSTRUCTOR);
        return Result.success("Team evaluations retrieved successfully", evaluationService.getEvaluationsByTeam(teamId));
    }

    @GetMapping("/section/{sectionId}")
    public ApiResponse<List<EvaluationResponse>> getEvaluationsBySection(@PathVariable Long sectionId, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN, Role.INSTRUCTOR);
        return Result.success("Section evaluations retrieved successfully", evaluationService.getEvaluationsBySection(sectionId));
    }

    @GetMapping("/target/{id}")
    public ApiResponse<List<EvaluationResponse>> getEvaluationsForStudent(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireSelfOrRole(session, id, Role.ADMIN, Role.INSTRUCTOR);
        return Result.success("Target evaluations retrieved successfully", evaluationService.getEvaluationsForStudent(id));
    }
}
