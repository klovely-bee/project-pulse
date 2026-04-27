package com.projectpulse.backend.team.service;

import com.projectpulse.backend.section.domain.Section;
import com.projectpulse.backend.section.repository.SectionRepository;
import com.projectpulse.backend.team.domain.Team;
import com.projectpulse.backend.team.dto.AssignUsersRequest;
import com.projectpulse.backend.team.dto.CreateTeamRequest;
import com.projectpulse.backend.team.dto.TeamResponse;
import com.projectpulse.backend.team.dto.TeamUserResponse;
import com.projectpulse.backend.team.dto.UpdateTeamRequest;
import com.projectpulse.backend.team.repository.TeamRepository;
import com.projectpulse.backend.user.domain.User;
import com.projectpulse.backend.user.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, SectionRepository sectionRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
    }

    public TeamResponse createTeam(CreateTeamRequest request) {
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + request.getSectionId()));

        Team team = Team.builder()
                .name(request.getName())
                .section(section)
                .build();

        Team savedTeam = teamRepository.save(team);
        return toTeamResponse(savedTeam);
    }

    public List<TeamResponse> getTeamsBySection(Long sectionId) {
        return teamRepository.findBySectionId(sectionId).stream()
                .map(this::toTeamResponse)
                .toList();
    }

    public TeamResponse getTeamById(Long id) {
        return toTeamResponse(findTeamById(id));
    }

    public TeamResponse updateTeam(UpdateTeamRequest request) {
        Team team = teamRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + request.getId()));

        team.setName(request.getName());

        if (request.getSectionId() != null) {
            Section section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> new RuntimeException("Section not found with id: " + request.getSectionId()));
            team.setSection(section);
        }

        Team savedTeam = teamRepository.save(team);
        return toTeamResponse(savedTeam);
    }

    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));

        teamRepository.delete(team);
    }

    public TeamResponse assignStudents(AssignUsersRequest request) {
        Team team = findTeamById(request.getTeamId());
        List<User> users = findUsersByIds(request.getUserIds());

        team.getStudents().addAll(users);

        Team savedTeam = teamRepository.save(team);
        return toTeamResponse(savedTeam);
    }

    public TeamResponse removeStudents(AssignUsersRequest request) {
        Team team = findTeamById(request.getTeamId());
        List<User> users = findUsersByIds(request.getUserIds());

        team.getStudents().removeAll(users);

        Team savedTeam = teamRepository.save(team);
        return toTeamResponse(savedTeam);
    }

    public TeamResponse assignInstructors(AssignUsersRequest request) {
        Team team = findTeamById(request.getTeamId());
        List<User> users = findUsersByIds(request.getUserIds());

        team.getInstructors().addAll(users);

        Team savedTeam = teamRepository.save(team);
        return toTeamResponse(savedTeam);
    }

    public TeamResponse removeInstructors(AssignUsersRequest request) {
        Team team = findTeamById(request.getTeamId());
        List<User> users = findUsersByIds(request.getUserIds());

        team.getInstructors().removeAll(users);

        Team savedTeam = teamRepository.save(team);
        return toTeamResponse(savedTeam);
    }

    private TeamResponse toTeamResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .sectionId(team.getSection() != null ? team.getSection().getId() : null)
                .students(team.getStudents().stream().map(this::toTeamUserResponse).toList())
                .instructors(team.getInstructors().stream().map(this::toTeamUserResponse).toList())
                .build();
    }

    private TeamUserResponse toTeamUserResponse(User user) {
        return TeamUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }

    private Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
    }

    private List<User> findUsersByIds(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);

        if (users.size() != userIds.size()) {
            throw new RuntimeException("One or more users not found");
        }

        return users;
    }
}
