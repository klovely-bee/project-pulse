package com.projectpulse.backend.war.controller;

import com.projectpulse.backend.auth.service.SessionUserService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.war.dto.CreateWarRequest;
import com.projectpulse.backend.war.dto.WarResponse;
import com.projectpulse.backend.war.service.WARService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/war")
public class WARController {

    private final WARService warService;
    private final SessionUserService sessionUserService;

    public WARController(WARService warService, SessionUserService sessionUserService) {
        this.warService = warService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping
    public ApiResponse<WarResponse> createWAR(@RequestBody CreateWarRequest request, HttpSession session) {
        var user = sessionUserService.requireRole(session, Role.STUDENT);
        if (!user.getId().equals(request.getUserId())) {
            throw new RuntimeException("Forbidden");
        }
        return Result.success("WAR created successfully", warService.createWAR(request));
    }

    @GetMapping("/student/{userId}")
    public ApiResponse<List<WarResponse>> getWARByStudent(@PathVariable Long userId, HttpSession session) {
        sessionUserService.requireSelfOrRole(session, userId, Role.ADMIN, Role.INSTRUCTOR);
        return Result.success("Student WARs retrieved successfully", warService.getWARByStudent(userId));
    }

    @GetMapping("/team/{teamId}")
    public ApiResponse<List<WarResponse>> getWARByTeam(@PathVariable Long teamId, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN, Role.INSTRUCTOR);
        return Result.success("Team WARs retrieved successfully", warService.getWARByTeam(teamId));
    }
}
