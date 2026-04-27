package com.projectpulse.backend.section.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.projectpulse.backend.config.SecurityConfig;
import com.projectpulse.backend.section.dto.CreateSectionRequest;
import com.projectpulse.backend.section.dto.SetActiveWeeksRequest;
import com.projectpulse.backend.section.dto.SectionResponse;
import com.projectpulse.backend.section.dto.UpdateSectionRequest;
import com.projectpulse.backend.section.service.SectionService;
import com.projectpulse.backend.week.dto.ActiveWeekRequest;
import com.projectpulse.backend.week.dto.ActiveWeekResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SectionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SectionService sectionService;

    @Test
    void createSection_shouldReturnApiResponse() throws Exception {
        CreateSectionRequest request = CreateSectionRequest.builder()
                .name("Section A")
                .semester("Fall")
                .year(2026)
                .rubricId(1L)
                .build();

        SectionResponse response = SectionResponse.builder()
                .id(1L)
                .name("Section A")
                .semester("Fall")
                .year(2026)
                .build();

        when(sectionService.createSection(any(CreateSectionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Section A",
                                  "semester": "Fall",
                                  "year": 2026,
                                  "rubricId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Section created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Section A"));
    }

    @Test
    void getAllSections_shouldReturnApiResponse() throws Exception {
        List<SectionResponse> response = List.of(
                SectionResponse.builder().id(1L).name("Section A").semester("Fall").year(2026).build(),
                SectionResponse.builder().id(2L).name("Section B").semester("Spring").year(2027).build());

        when(sectionService.getAllSections()).thenReturn(response);

        mockMvc.perform(get("/api/sections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Sections retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].name").value("Section B"));
    }

    @Test
    void getSectionById_shouldReturnApiResponse() throws Exception {
        SectionResponse response = SectionResponse.builder()
                .id(1L)
                .name("Section A")
                .semester("Fall")
                .year(2026)
                .build();

        when(sectionService.getSectionById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/sections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Section retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Section A"));
    }

    @Test
    void updateSection_shouldReturnApiResponse() throws Exception {
        UpdateSectionRequest request = UpdateSectionRequest.builder()
                .id(1L)
                .name("Section A Updated")
                .semester("Fall")
                .year(2026)
                .rubricId(1L)
                .build();

        SectionResponse response = SectionResponse.builder()
                .id(1L)
                .name("Section A Updated")
                .semester("Fall")
                .year(2026)
                .build();

        when(sectionService.updateSection(any(UpdateSectionRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "name": "Section A Updated",
                                  "semester": "Fall",
                                  "year": 2026,
                                  "rubricId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Section updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Section A Updated"));
    }

    @Test
    void setActiveWeeks_shouldReturnApiResponse() throws Exception {
        SetActiveWeeksRequest request = SetActiveWeeksRequest.builder()
                .sectionId(1L)
                .weeks(List.of(
                        ActiveWeekRequest.builder()
                                .weekNumber(1)
                                .startDate(java.time.LocalDate.of(2026, 8, 24))
                                .endDate(java.time.LocalDate.of(2026, 8, 30))
                                .build()))
                .build();

        List<ActiveWeekResponse> response = List.of(
                ActiveWeekResponse.builder()
                        .id(1L)
                        .weekNumber(1)
                        .startDate(java.time.LocalDate.of(2026, 8, 24))
                        .endDate(java.time.LocalDate.of(2026, 8, 30))
                        .build());

        when(sectionService.setActiveWeeks(any(SetActiveWeeksRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/sections/weeks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sectionId": 1,
                                  "weeks": [
                                    {
                                      "weekNumber": 1,
                                      "startDate": "2026-08-24",
                                      "endDate": "2026-08-30"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Active weeks updated successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].weekNumber").value(1));
    }

    @Test
    void getActiveWeeks_shouldReturnApiResponse() throws Exception {
        List<ActiveWeekResponse> response = List.of(
                ActiveWeekResponse.builder()
                        .id(1L)
                        .weekNumber(1)
                        .startDate(java.time.LocalDate.of(2026, 8, 24))
                        .endDate(java.time.LocalDate.of(2026, 8, 30))
                        .build(),
                ActiveWeekResponse.builder()
                        .id(2L)
                        .weekNumber(2)
                        .startDate(java.time.LocalDate.of(2026, 8, 31))
                        .endDate(java.time.LocalDate.of(2026, 9, 6))
                        .build());

        when(sectionService.getActiveWeeks(1L)).thenReturn(response);

        mockMvc.perform(get("/api/sections/1/weeks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Active weeks retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].weekNumber").value(2));
    }
}
