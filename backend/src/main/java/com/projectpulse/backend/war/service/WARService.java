package com.projectpulse.backend.war.service;

import com.projectpulse.backend.team.domain.Team;
import com.projectpulse.backend.team.repository.TeamRepository;
import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.user.domain.User;
import com.projectpulse.backend.user.repository.UserRepository;
import com.projectpulse.backend.war.domain.WAR;
import com.projectpulse.backend.war.dto.CreateWarRequest;
import com.projectpulse.backend.war.dto.WarResponse;
import com.projectpulse.backend.war.repository.WARRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WARService {

    private final WARRepository warRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public WARService(WARRepository warRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.warRepository = warRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public WarResponse createWAR(CreateWarRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + request.getTeamId()));

        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("WARs can only be submitted by students");
        }

        boolean studentOnTeam = team.getStudents().stream().anyMatch(student -> student.getId().equals(user.getId()));
        if (!studentOnTeam) {
            throw new RuntimeException("Student is not assigned to the specified team");
        }

        validateWeekForTeam(team, request.getWeekNumber());

        WAR war = WAR.builder()
                .userId(request.getUserId())
                .teamId(request.getTeamId())
                .weekNumber(request.getWeekNumber())
                .content(request.getContent())
                .build();

        return toWarResponse(warRepository.save(war));
    }

    public List<WarResponse> getWARByStudent(Long userId) {
        return warRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toWarResponse)
                .toList();
    }

    public List<WarResponse> getWARByTeam(Long teamId) {
        return warRepository.findByTeamIdOrderByCreatedAtDesc(teamId).stream()
                .map(this::toWarResponse)
                .toList();
    }

    private WarResponse toWarResponse(WAR war) {
        return WarResponse.builder()
                .id(war.getId())
                .userId(war.getUserId())
                .teamId(war.getTeamId())
                .weekNumber(war.getWeekNumber())
                .content(war.getContent())
                .createdAt(war.getCreatedAt())
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
