package com.projectpulse.backend.rubric.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.projectpulse.backend.config.SecurityConfig;
import com.projectpulse.backend.rubric.dto.CreateRubricRequest;
import com.projectpulse.backend.rubric.dto.RubricCriterionRequest;
import com.projectpulse.backend.rubric.dto.RubricResponse;
import com.projectpulse.backend.rubric.service.RubricService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RubricController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
class RubricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RubricService rubricService;

    @Test
    void createRubric_shouldReturnApiResponse() throws Exception {
        CreateRubricRequest request = CreateRubricRequest.builder()
                .name("Senior Design Rubric")
                .description("Rubric description")
                .criteria(List.of(
                        RubricCriterionRequest.builder()
                                .criterionName("Technical Quality")
                                .description("Implementation quality")
                                .maxScore(40)
                                .build()))
                .build();

        RubricResponse response = RubricResponse.builder()
                .id(1L)
                .name("Senior Design Rubric")
                .description("Rubric description")
                .criteria(request.getCriteria())
                .build();

        when(rubricService.createRubric(any(CreateRubricRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/rubrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Senior Design Rubric",
                                  "description": "Rubric description",
                                  "criteria": [
                                    {
                                      "criterionName": "Technical Quality",
                                      "description": "Implementation quality",
                                      "maxScore": 40
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Rubric created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Senior Design Rubric"))
                .andExpect(jsonPath("$.data.criteria[0].criterionName").value("Technical Quality"));
    }
}
