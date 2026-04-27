package com.projectpulse.backend.war.controller;

import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.war.dto.CreateWarRequest;
import com.projectpulse.backend.war.dto.WarResponse;
import com.projectpulse.backend.war.service.WARService;
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

    public WARController(WARService warService) {
        this.warService = warService;
    }

    @PostMapping
    public ApiResponse<WarResponse> createWAR(@RequestBody CreateWarRequest request) {
        return Result.success("WAR created successfully", warService.createWAR(request));
    }

    @GetMapping("/student/{userId}")
    public ApiResponse<List<WarResponse>> getWARByStudent(@PathVariable Long userId) {
        return Result.success("Student WARs retrieved successfully", warService.getWARByStudent(userId));
    }

    @GetMapping("/team/{teamId}")
    public ApiResponse<List<WarResponse>> getWARByTeam(@PathVariable Long teamId) {
        return Result.success("Team WARs retrieved successfully", warService.getWARByTeam(teamId));
    }
}
