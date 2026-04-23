package com.projectpulse.backend.team.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.projectpulse.backend.config.SecurityConfig;
import com.projectpulse.backend.team.dto.AssignUsersRequest;
import com.projectpulse.backend.team.dto.CreateTeamRequest;
import com.projectpulse.backend.team.dto.TeamResponse;
import com.projectpulse.backend.team.dto.UpdateTeamRequest;
import com.projectpulse.backend.team.service.TeamService;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamService teamService;

    @Test
    void createTeam_shouldReturnApiResponse() throws Exception {
        CreateTeamRequest request = CreateTeamRequest.builder()
                .name("Team Alpha")
                .sectionId(1L)
                .build();

        TeamResponse response = TeamResponse.builder()
                .id(1L)
                .name("Team Alpha")
                .build();

        when(teamService.createTeam(any(CreateTeamRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Team Alpha",
                                  "sectionId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Team created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Team Alpha"));
    }

    @Test
    void getTeamsBySection_shouldReturnApiResponse() throws Exception {
        List<TeamResponse> response = List.of(
                TeamResponse.builder().id(1L).name("Team Alpha").build(),
                TeamResponse.builder().id(2L).name("Team Beta").build());

        when(teamService.getTeamsBySection(1L)).thenReturn(response);

        mockMvc.perform(get("/api/teams/section/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Teams retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].name").value("Team Beta"));
    }

    @Test
    void updateTeam_shouldReturnApiResponse() throws Exception {
        UpdateTeamRequest request = UpdateTeamRequest.builder()
                .id(1L)
                .name("Team Alpha Updated")
                .build();

        TeamResponse response = TeamResponse.builder()
                .id(1L)
                .name("Team Alpha Updated")
                .build();

        when(teamService.updateTeam(any(UpdateTeamRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "name": "Team Alpha Updated"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Team updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Team Alpha Updated"));
    }

    @Test
    void deleteTeam_shouldReturnApiResponse() throws Exception {
        doNothing().when(teamService).deleteTeam(1L);

        mockMvc.perform(delete("/api/teams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Team deleted successfully"))
                .andExpect(jsonPath("$.data").value(Matchers.nullValue()));
    }

    @Test
    void assignStudents_shouldReturnApiResponse() throws Exception {
        TeamResponse response = TeamResponse.builder()
                .id(1L)
                .name("Team Alpha")
                .build();

        when(teamService.assignStudents(any(AssignUsersRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/teams/students/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "teamId": 1,
                                  "userIds": [101, 102]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Students assigned successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Team Alpha"));
    }

    @Test
    void removeStudents_shouldReturnApiResponse() throws Exception {
        TeamResponse response = TeamResponse.builder()
                .id(1L)
                .name("Team Alpha")
                .build();

        when(teamService.removeStudents(any(AssignUsersRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/teams/students/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "teamId": 1,
                                  "userIds": [101]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Students removed successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Team Alpha"));
    }

    @Test
    void assignInstructors_shouldReturnApiResponse() throws Exception {
        TeamResponse response = TeamResponse.builder()
                .id(2L)
                .name("Team Beta")
                .build();

        when(teamService.assignInstructors(any(AssignUsersRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/teams/instructors/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "teamId": 2,
                                  "userIds": [201, 202]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Instructors assigned successfully"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value("Team Beta"));
    }

    @Test
    void removeInstructors_shouldReturnApiResponse() throws Exception {
        TeamResponse response = TeamResponse.builder()
                .id(2L)
                .name("Team Beta")
                .build();

        when(teamService.removeInstructors(any(AssignUsersRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/teams/instructors/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "teamId": 2,
                                  "userIds": [202]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Instructors removed successfully"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value("Team Beta"));
    }
}
