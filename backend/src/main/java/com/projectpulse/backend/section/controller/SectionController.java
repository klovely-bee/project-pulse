package com.projectpulse.backend.section.controller;

import com.projectpulse.backend.auth.service.SessionUserService;
import com.projectpulse.backend.section.dto.CreateSectionRequest;
import com.projectpulse.backend.section.dto.SetActiveWeeksRequest;
import com.projectpulse.backend.section.dto.SectionResponse;
import com.projectpulse.backend.section.dto.UpdateSectionRequest;
import com.projectpulse.backend.section.service.SectionService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.week.dto.ActiveWeekResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionService sectionService;
    private final SessionUserService sessionUserService;

    public SectionController(SectionService sectionService, SessionUserService sessionUserService) {
        this.sectionService = sectionService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping
    public ApiResponse<SectionResponse> createSection(@RequestBody CreateSectionRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Section created successfully", sectionService.createSection(request));
    }

    @GetMapping
    public ApiResponse<List<SectionResponse>> getAllSections(HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Sections retrieved successfully", sectionService.getAllSections());
    }

    @GetMapping("/{id}")
    public ApiResponse<SectionResponse> getSectionById(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Section retrieved successfully", sectionService.getSectionById(id));
    }

    @PutMapping
    public ApiResponse<SectionResponse> updateSection(@RequestBody UpdateSectionRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Section updated successfully", sectionService.updateSection(request));
    }

    @PostMapping("/weeks")
    public ApiResponse<List<ActiveWeekResponse>> setActiveWeeks(@RequestBody SetActiveWeeksRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Active weeks updated successfully", sectionService.setActiveWeeks(request));
    }

    @GetMapping("/{id}/weeks")
    public ApiResponse<List<ActiveWeekResponse>> getActiveWeeks(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Active weeks retrieved successfully", sectionService.getActiveWeeks(id));
    }
}
