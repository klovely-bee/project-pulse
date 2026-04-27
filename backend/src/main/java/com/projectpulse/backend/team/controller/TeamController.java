package com.projectpulse.backend.team.controller;

import com.projectpulse.backend.auth.service.SessionUserService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.team.dto.AssignUsersRequest;
import com.projectpulse.backend.team.dto.CreateTeamRequest;
import com.projectpulse.backend.team.dto.TeamResponse;
import com.projectpulse.backend.team.dto.UpdateTeamRequest;
import com.projectpulse.backend.team.service.TeamService;
import com.projectpulse.backend.user.domain.Role;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final SessionUserService sessionUserService;

    public TeamController(TeamService teamService, SessionUserService sessionUserService) {
        this.teamService = teamService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping
    public ApiResponse<TeamResponse> createTeam(@RequestBody CreateTeamRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Team created successfully", teamService.createTeam(request));
    }

    @GetMapping("/section/{sectionId}")
    public ApiResponse<List<TeamResponse>> getTeamsBySection(@PathVariable Long sectionId, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN, Role.INSTRUCTOR);
        return Result.success("Teams retrieved successfully", teamService.getTeamsBySection(sectionId));
    }

    @GetMapping("/{id}")
    public ApiResponse<TeamResponse> getTeamById(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN, Role.INSTRUCTOR);
        return Result.success("Team retrieved successfully", teamService.getTeamById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<TeamResponse> updateTeam(@PathVariable Long id, @RequestBody UpdateTeamRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        request.setId(id);
        return Result.success("Team updated successfully", teamService.updateTeam(request));
    }

    @PutMapping
    public ApiResponse<TeamResponse> updateTeam(@RequestBody UpdateTeamRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Team updated successfully", teamService.updateTeam(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTeam(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        teamService.deleteTeam(id);
        return Result.success("Team deleted successfully");
    }

    @PostMapping("/students/assign")
    public ApiResponse<TeamResponse> assignStudents(@RequestBody AssignUsersRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Students assigned successfully", teamService.assignStudents(request));
    }

    @PostMapping("/students/remove")
    public ApiResponse<TeamResponse> removeStudents(@RequestBody AssignUsersRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Students removed successfully", teamService.removeStudents(request));
    }

    @PostMapping("/instructors/assign")
    public ApiResponse<TeamResponse> assignInstructors(@RequestBody AssignUsersRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Instructors assigned successfully", teamService.assignInstructors(request));
    }

    @PostMapping("/instructors/remove")
    public ApiResponse<TeamResponse> removeInstructors(@RequestBody AssignUsersRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Instructors removed successfully", teamService.removeInstructors(request));
    }
}
