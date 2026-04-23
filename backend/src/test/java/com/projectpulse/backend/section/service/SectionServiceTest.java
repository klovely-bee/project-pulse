package com.projectpulse.backend.section.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.projectpulse.backend.rubric.domain.Rubric;
import com.projectpulse.backend.rubric.repository.RubricRepository;
import com.projectpulse.backend.section.domain.Section;
import com.projectpulse.backend.section.dto.CreateSectionRequest;
import com.projectpulse.backend.section.dto.SetActiveWeeksRequest;
import com.projectpulse.backend.section.dto.SectionResponse;
import com.projectpulse.backend.section.dto.UpdateSectionRequest;
import com.projectpulse.backend.section.repository.SectionRepository;
import com.projectpulse.backend.week.domain.ActiveWeek;
import com.projectpulse.backend.week.dto.ActiveWeekRequest;
import com.projectpulse.backend.week.dto.ActiveWeekResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private RubricRepository rubricRepository;

    @InjectMocks
    private SectionService sectionService;

    @Test
    void createSection_shouldReturnSectionResponse_whenRubricExists() {
        CreateSectionRequest request = CreateSectionRequest.builder()
                .name("Section A")
                .semester("Fall")
                .year(2026)
                .rubricId(1L)
                .build();

        Rubric rubric = Rubric.builder()
                .id(1L)
                .name("Rubric 1")
                .build();

        when(rubricRepository.findById(1L)).thenReturn(Optional.of(rubric));
        when(sectionRepository.save(any(Section.class))).thenAnswer(invocation -> {
            Section section = invocation.getArgument(0);
            section.setId(2L);
            return section;
        });

        SectionResponse response = sectionService.createSection(request);

        assertEquals(2L, response.getId());
        assertEquals("Section A", response.getName());
        assertEquals("Fall", response.getSemester());
        assertEquals(2026, response.getYear());
        verify(sectionRepository).save(any(Section.class));
    }

    @Test
    void createSection_shouldThrowException_whenRubricDoesNotExist() {
        CreateSectionRequest request = CreateSectionRequest.builder()
                .name("Section A")
                .semester("Fall")
                .year(2026)
                .rubricId(99L)
                .build();

        when(rubricRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sectionService.createSection(request));

        assertEquals("Rubric not found with id: 99", exception.getMessage());
    }

    @Test
    void getSectionById_shouldReturnSectionResponse_whenSectionExists() {
        Section section = Section.builder()
                .id(5L)
                .name("Section B")
                .semester("Spring")
                .year(2027)
                .build();

        when(sectionRepository.findById(5L)).thenReturn(Optional.of(section));

        SectionResponse response = sectionService.getSectionById(5L);

        assertEquals(5L, response.getId());
        assertEquals("Section B", response.getName());
        assertEquals("Spring", response.getSemester());
        assertEquals(2027, response.getYear());
    }

    @Test
    void getSectionById_shouldThrowException_whenSectionDoesNotExist() {
        when(sectionRepository.findById(88L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sectionService.getSectionById(88L));

        assertEquals("Section not found with id: 88", exception.getMessage());
    }

    @Test
    void getAllSections_shouldReturnAllSections() {
        when(sectionRepository.findAll()).thenReturn(List.of(
                Section.builder().id(1L).name("Section A").semester("Fall").year(2026).build(),
                Section.builder().id(2L).name("Section B").semester("Spring").year(2027).build()));

        List<SectionResponse> response = sectionService.getAllSections();

        assertEquals(2, response.size());
        assertEquals("Section A", response.get(0).getName());
        assertEquals("Section B", response.get(1).getName());
    }

    @Test
    void updateSection_shouldReturnUpdatedSection_whenSectionAndRubricExist() {
        UpdateSectionRequest request = UpdateSectionRequest.builder()
                .id(4L)
                .name("Updated Section")
                .semester("Fall")
                .year(2028)
                .rubricId(2L)
                .build();

        Section section = Section.builder()
                .id(4L)
                .name("Old Section")
                .semester("Spring")
                .year(2027)
                .build();

        Rubric rubric = Rubric.builder()
                .id(2L)
                .name("Updated Rubric")
                .build();

        when(sectionRepository.findById(4L)).thenReturn(Optional.of(section));
        when(rubricRepository.findById(2L)).thenReturn(Optional.of(rubric));
        when(sectionRepository.save(any(Section.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SectionResponse response = sectionService.updateSection(request);

        assertEquals(4L, response.getId());
        assertEquals("Updated Section", response.getName());
        assertEquals("Fall", response.getSemester());
        assertEquals(2028, response.getYear());
    }

    @Test
    void updateSection_shouldThrowException_whenSectionDoesNotExist() {
        UpdateSectionRequest request = UpdateSectionRequest.builder()
                .id(44L)
                .name("Updated Section")
                .semester("Fall")
                .year(2028)
                .rubricId(2L)
                .build();

        when(sectionRepository.findById(44L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sectionService.updateSection(request));

        assertEquals("Section not found with id: 44", exception.getMessage());
    }

    @Test
    void updateSection_shouldThrowException_whenRubricDoesNotExist() {
        UpdateSectionRequest request = UpdateSectionRequest.builder()
                .id(4L)
                .name("Updated Section")
                .semester("Fall")
                .year(2028)
                .rubricId(22L)
                .build();

        Section section = Section.builder()
                .id(4L)
                .name("Old Section")
                .build();

        when(sectionRepository.findById(4L)).thenReturn(Optional.of(section));
        when(rubricRepository.findById(22L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sectionService.updateSection(request));

        assertEquals("Rubric not found with id: 22", exception.getMessage());
    }

    @Test
    void setActiveWeeks_shouldReplaceWeeksAndReturnResponses() {
        SetActiveWeeksRequest request = SetActiveWeeksRequest.builder()
                .sectionId(3L)
                .weeks(List.of(
                        ActiveWeekRequest.builder()
                                .weekNumber(1)
                                .startDate(java.time.LocalDate.of(2026, 8, 24))
                                .endDate(java.time.LocalDate.of(2026, 8, 30))
                                .build(),
                        ActiveWeekRequest.builder()
                                .weekNumber(2)
                                .startDate(java.time.LocalDate.of(2026, 8, 31))
                                .endDate(java.time.LocalDate.of(2026, 9, 6))
                                .build()))
                .build();

        Section section = Section.builder()
                .id(3L)
                .name("Section A")
                .activeWeeks(new java.util.ArrayList<>(List.of(
                        ActiveWeek.builder().id(99L).weekNumber(99).build())))
                .build();

        when(sectionRepository.findById(3L)).thenReturn(Optional.of(section));
        when(sectionRepository.save(any(Section.class))).thenAnswer(invocation -> {
            Section savedSection = invocation.getArgument(0);
            long id = 10L;
            for (ActiveWeek activeWeek : savedSection.getActiveWeeks()) {
                activeWeek.setId(id++);
            }
            return savedSection;
        });

        List<ActiveWeekResponse> response = sectionService.setActiveWeeks(request);

        assertEquals(2, response.size());
        assertEquals(1, response.get(0).getWeekNumber());
        assertEquals(2, response.get(1).getWeekNumber());
        verify(sectionRepository).save(any(Section.class));
    }

    @Test
    void setActiveWeeks_shouldThrowException_whenSectionDoesNotExist() {
        SetActiveWeeksRequest request = SetActiveWeeksRequest.builder()
                .sectionId(30L)
                .weeks(List.of())
                .build();

        when(sectionRepository.findById(30L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sectionService.setActiveWeeks(request));

        assertEquals("Section not found with id: 30", exception.getMessage());
    }

    @Test
    void getActiveWeeks_shouldReturnActiveWeeks_whenSectionExists() {
        Section section = Section.builder()
                .id(6L)
                .name("Section Weeks")
                .activeWeeks(new java.util.ArrayList<>(List.of(
                        ActiveWeek.builder()
                                .id(1L)
                                .weekNumber(1)
                                .startDate(java.time.LocalDate.of(2026, 8, 24))
                                .endDate(java.time.LocalDate.of(2026, 8, 30))
                                .build(),
                        ActiveWeek.builder()
                                .id(2L)
                                .weekNumber(2)
                                .startDate(java.time.LocalDate.of(2026, 8, 31))
                                .endDate(java.time.LocalDate.of(2026, 9, 6))
                                .build())))
                .build();

        when(sectionRepository.findById(6L)).thenReturn(Optional.of(section));

        List<ActiveWeekResponse> response = sectionService.getActiveWeeks(6L);

        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals(2L, response.get(1).getId());
    }

    @Test
    void getActiveWeeks_shouldThrowException_whenSectionDoesNotExist() {
        when(sectionRepository.findById(60L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sectionService.getActiveWeeks(60L));

        assertEquals("Section not found with id: 60", exception.getMessage());
    }
}
