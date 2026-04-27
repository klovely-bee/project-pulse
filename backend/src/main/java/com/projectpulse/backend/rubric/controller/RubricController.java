package com.projectpulse.backend.rubric.controller;

import com.projectpulse.backend.auth.service.SessionUserService;
import com.projectpulse.backend.rubric.dto.CreateRubricRequest;
import com.projectpulse.backend.rubric.dto.RubricResponse;
import com.projectpulse.backend.rubric.service.RubricService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.user.domain.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rubrics")
public class RubricController {

    private final RubricService rubricService;
    private final SessionUserService sessionUserService;

    public RubricController(RubricService rubricService, SessionUserService sessionUserService) {
        this.rubricService = rubricService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping
    public ApiResponse<RubricResponse> createRubric(@RequestBody CreateRubricRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Rubric created successfully", rubricService.createRubric(request));
    }
}
