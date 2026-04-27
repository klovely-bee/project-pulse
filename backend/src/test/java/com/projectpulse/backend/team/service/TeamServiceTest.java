package com.projectpulse.backend.team.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.projectpulse.backend.section.domain.Section;
import com.projectpulse.backend.section.repository.SectionRepository;
import com.projectpulse.backend.team.domain.Team;
import com.projectpulse.backend.team.dto.AssignUsersRequest;
import com.projectpulse.backend.team.dto.CreateTeamRequest;
import com.projectpulse.backend.team.dto.TeamResponse;
import com.projectpulse.backend.team.dto.UpdateTeamRequest;
import com.projectpulse.backend.team.repository.TeamRepository;
import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.user.domain.User;
import com.projectpulse.backend.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    void createTeam_shouldReturnTeamResponse_whenSectionExists() {
        CreateTeamRequest request = CreateTeamRequest.builder()
                .name("Team Alpha")
                .sectionId(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .name("Section A")
                .build();

        when(sectionRepository.findById(1L)).thenReturn(Optional.of(section));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> {
            Team team = invocation.getArgument(0);
            team.setId(10L);
            return team;
        });

        TeamResponse response = teamService.createTeam(request);

        assertEquals(10L, response.getId());
        assertEquals("Team Alpha", response.getName());
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void createTeam_shouldThrowException_whenSectionDoesNotExist() {
        CreateTeamRequest request = CreateTeamRequest.builder()
                .name("Team Alpha")
                .sectionId(99L)
                .build();

        when(sectionRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> teamService.createTeam(request));

        assertEquals("Section not found with id: 99", exception.getMessage());
    }

    @Test
    void updateTeam_shouldUpdateNameAndReturnResponse() {
        UpdateTeamRequest request = UpdateTeamRequest.builder()
                .id(5L)
                .name("Team Beta")
                .build();

        Team team = Team.builder()
                .id(5L)
                .name("Team Alpha")
                .build();

        when(teamRepository.findById(5L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TeamResponse response = teamService.updateTeam(request);

        assertEquals(5L, response.getId());
        assertEquals("Team Beta", response.getName());
    }

    @Test
    void getTeamsBySection_shouldReturnMappedTeams() {
        when(teamRepository.findBySectionId(1L)).thenReturn(List.of(
                Team.builder().id(1L).name("Team Alpha").build(),
                Team.builder().id(2L).name("Team Beta").build()));

        List<TeamResponse> response = teamService.getTeamsBySection(1L);

        assertEquals(2, response.size());
        assertEquals("Team Alpha", response.get(0).getName());
        assertEquals("Team Beta", response.get(1).getName());
    }

    @Test
    void updateTeam_shouldThrowException_whenTeamDoesNotExist() {
        UpdateTeamRequest request = UpdateTeamRequest.builder()
                .id(99L)
                .name("Missing Team")
                .build();

        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> teamService.updateTeam(request));

        assertEquals("Team not found with id: 99", exception.getMessage());
    }

    @Test
    void deleteTeam_shouldDeleteExistingTeam() {
        Team team = Team.builder()
                .id(7L)
                .name("Team Alpha")
                .build();

        when(teamRepository.findById(7L)).thenReturn(Optional.of(team));

        teamService.deleteTeam(7L);

        verify(teamRepository).delete(team);
    }

    @Test
    void deleteTeam_shouldThrowException_whenTeamDoesNotExist() {
        when(teamRepository.findById(77L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> teamService.deleteTeam(77L));

        assertEquals("Team not found with id: 77", exception.getMessage());
    }

    @Test
    void assignStudents_shouldAddUsersToStudentsAndReturnResponse() {
        AssignUsersRequest request = AssignUsersRequest.builder()
                .teamId(1L)
                .userIds(List.of(10L, 11L))
                .build();

        Team team = Team.builder()
                .id(1L)
                .name("Team Alpha")
                .build();

        User userOne = User.builder().id(10L).email("student1@tcu.edu").role(Role.STUDENT).build();
        User userTwo = User.builder().id(11L).email("student2@tcu.edu").role(Role.STUDENT).build();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findAllById(request.getUserIds())).thenReturn(List.of(userOne, userTwo));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TeamResponse response = teamService.assignStudents(request);

        assertEquals(1L, response.getId());
        assertEquals(2, team.getStudents().size());
    }

    @Test
    void removeStudents_shouldRemoveUsersFromStudentsAndReturnResponse() {
        AssignUsersRequest request = AssignUsersRequest.builder()
                .teamId(2L)
                .userIds(List.of(20L))
                .build();

        User student = User.builder().id(20L).email("student@tcu.edu").role(Role.STUDENT).build();
        java.util.Set<User> students = new java.util.HashSet<>();
        students.add(student);

        Team team = Team.builder()
                .id(2L)
                .name("Team Beta")
                .students(students)
                .build();

        when(teamRepository.findById(2L)).thenReturn(Optional.of(team));
        when(userRepository.findAllById(request.getUserIds())).thenReturn(List.of(student));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TeamResponse response = teamService.removeStudents(request);

        assertEquals(2L, response.getId());
        assertEquals(0, team.getStudents().size());
    }

    @Test
    void assignInstructors_shouldAddUsersToInstructorsAndReturnResponse() {
        AssignUsersRequest request = AssignUsersRequest.builder()
                .teamId(3L)
                .userIds(List.of(30L))
                .build();

        Team team = Team.builder()
                .id(3L)
                .name("Team Gamma")
                .build();

        User instructor = User.builder().id(30L).email("instructor@tcu.edu").role(Role.INSTRUCTOR).build();

        when(teamRepository.findById(3L)).thenReturn(Optional.of(team));
        when(userRepository.findAllById(request.getUserIds())).thenReturn(List.of(instructor));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TeamResponse response = teamService.assignInstructors(request);

        assertEquals(3L, response.getId());
        assertEquals(1, team.getInstructors().size());
    }

    @Test
    void removeInstructors_shouldRemoveUsersFromInstructorsAndReturnResponse() {
        AssignUsersRequest request = AssignUsersRequest.builder()
                .teamId(4L)
                .userIds(List.of(40L))
                .build();

        User instructor = User.builder().id(40L).email("instructor2@tcu.edu").role(Role.INSTRUCTOR).build();
        java.util.Set<User> instructors = new java.util.HashSet<>();
        instructors.add(instructor);

        Team team = Team.builder()
                .id(4L)
                .name("Team Delta")
                .instructors(instructors)
                .build();

        when(teamRepository.findById(4L)).thenReturn(Optional.of(team));
        when(userRepository.findAllById(request.getUserIds())).thenReturn(List.of(instructor));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TeamResponse response = teamService.removeInstructors(request);

        assertEquals(4L, response.getId());
        assertEquals(0, team.getInstructors().size());
    }

    @Test
    void assignStudents_shouldThrowException_whenUserNotFound() {
        AssignUsersRequest request = AssignUsersRequest.builder()
                .teamId(5L)
                .userIds(List.of(50L, 51L))
                .build();

        Team team = Team.builder()
                .id(5L)
                .name("Team Epsilon")
                .build();

        User user = User.builder().id(50L).email("student3@tcu.edu").role(Role.STUDENT).build();

        when(teamRepository.findById(5L)).thenReturn(Optional.of(team));
        when(userRepository.findAllById(request.getUserIds())).thenReturn(List.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> teamService.assignStudents(request));

        assertEquals("One or more users not found", exception.getMessage());
    }
}
