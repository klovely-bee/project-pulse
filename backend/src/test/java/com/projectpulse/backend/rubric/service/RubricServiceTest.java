package com.projectpulse.backend.rubric.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.projectpulse.backend.rubric.domain.Rubric;
import com.projectpulse.backend.rubric.domain.RubricCriterion;
import com.projectpulse.backend.rubric.dto.CreateRubricRequest;
import com.projectpulse.backend.rubric.dto.RubricCriterionRequest;
import com.projectpulse.backend.rubric.dto.RubricResponse;
import com.projectpulse.backend.rubric.repository.RubricRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RubricServiceTest {

    @Mock
    private RubricRepository rubricRepository;

    @InjectMocks
    private RubricService rubricService;

    @Test
    void createRubric_shouldCreateRubricWithCriteria() {
        CreateRubricRequest request = CreateRubricRequest.builder()
                .name("Senior Design Rubric")
                .description("Rubric description")
                .criteria(List.of(
                        RubricCriterionRequest.builder()
                                .criterionName("Technical Quality")
                                .description("Quality of implementation")
                                .maxScore(40)
                                .build(),
                        RubricCriterionRequest.builder()
                                .criterionName("Presentation")
                                .description("Presentation quality")
                                .maxScore(20)
                                .build()))
                .build();

        when(rubricRepository.save(any(Rubric.class))).thenAnswer(invocation -> {
            Rubric rubric = invocation.getArgument(0);
            rubric.setId(1L);
            long criterionId = 10L;
            for (RubricCriterion criterion : rubric.getCriteria()) {
                criterion.setId(criterionId++);
            }
            return rubric;
        });

        RubricResponse response = rubricService.createRubric(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Senior Design Rubric", response.getName());
        assertEquals(2, response.getCriteria().size());
        assertEquals("Technical Quality", response.getCriteria().get(0).getCriterionName());

        ArgumentCaptor<Rubric> rubricCaptor = ArgumentCaptor.forClass(Rubric.class);
        verify(rubricRepository).save(rubricCaptor.capture());
        Rubric savedRubric = rubricCaptor.getValue();
        assertEquals(2, savedRubric.getCriteria().size());
        assertEquals(savedRubric, savedRubric.getCriteria().get(0).getRubric());
    }

    @Test
    void createRubric_shouldCreateRubricWhenCriteriaIsNull() {
        CreateRubricRequest request = CreateRubricRequest.builder()
                .name("Simple Rubric")
                .description("No criteria")
                .criteria(null)
                .build();

        when(rubricRepository.save(any(Rubric.class))).thenAnswer(invocation -> {
            Rubric rubric = invocation.getArgument(0);
            rubric.setId(2L);
            return rubric;
        });

        RubricResponse response = rubricService.createRubric(request);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("Simple Rubric", response.getName());
        assertNotNull(response.getCriteria());
        assertEquals(0, response.getCriteria().size());
    }

    @Test
    void createRubric_shouldCreateRubricWhenCriteriaIsEmpty() {
        CreateRubricRequest request = CreateRubricRequest.builder()
                .name("Empty Rubric")
                .description("Empty criteria")
                .criteria(List.of())
                .build();

        when(rubricRepository.save(any(Rubric.class))).thenAnswer(invocation -> {
            Rubric rubric = invocation.getArgument(0);
            rubric.setId(3L);
            return rubric;
        });

        RubricResponse response = rubricService.createRubric(request);

        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals("Empty Rubric", response.getName());
        assertNotNull(response.getCriteria());
        assertEquals(0, response.getCriteria().size());
    }
}
