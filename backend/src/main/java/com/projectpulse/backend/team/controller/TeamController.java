package com.projectpulse.backend.team.controller;

import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.team.dto.AssignUsersRequest;
import com.projectpulse.backend.team.dto.CreateTeamRequest;
import com.projectpulse.backend.team.dto.TeamResponse;
import com.projectpulse.backend.team.dto.UpdateTeamRequest;
import com.projectpulse.backend.team.service.TeamService;
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

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ApiResponse<TeamResponse> createTeam(@RequestBody CreateTeamRequest request) {
        return Result.success("Team created successfully", teamService.createTeam(request));
    }

    @GetMapping("/section/{sectionId}")
    public ApiResponse<List<TeamResponse>> getTeamsBySection(@PathVariable Long sectionId) {
        return Result.success("Teams retrieved successfully", teamService.getTeamsBySection(sectionId));
    }

    @PutMapping
    public ApiResponse<TeamResponse> updateTeam(@RequestBody UpdateTeamRequest request) {
        return Result.success("Team updated successfully", teamService.updateTeam(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return Result.success("Team deleted successfully");
    }

    @PostMapping("/students/assign")
    public ApiResponse<TeamResponse> assignStudents(@RequestBody AssignUsersRequest request) {
        return Result.success("Students assigned successfully", teamService.assignStudents(request));
    }

    @PostMapping("/students/remove")
    public ApiResponse<TeamResponse> removeStudents(@RequestBody AssignUsersRequest request) {
        return Result.success("Students removed successfully", teamService.removeStudents(request));
    }

    @PostMapping("/instructors/assign")
    public ApiResponse<TeamResponse> assignInstructors(@RequestBody AssignUsersRequest request) {
        return Result.success("Instructors assigned successfully", teamService.assignInstructors(request));
    }

    @PostMapping("/instructors/remove")
    public ApiResponse<TeamResponse> removeInstructors(@RequestBody AssignUsersRequest request) {
        return Result.success("Instructors removed successfully", teamService.removeInstructors(request));
    }
}
