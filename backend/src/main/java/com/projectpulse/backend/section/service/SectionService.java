package com.projectpulse.backend.section.service;

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
import org.springframework.stereotype.Service;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final RubricRepository rubricRepository;

    public SectionService(SectionRepository sectionRepository, RubricRepository rubricRepository) {
        this.sectionRepository = sectionRepository;
        this.rubricRepository = rubricRepository;
    }

    public SectionResponse createSection(CreateSectionRequest request) {
        Rubric rubric = rubricRepository.findById(request.getRubricId())
                .orElseThrow(() -> new RuntimeException("Rubric not found with id: " + request.getRubricId()));

        Section section = Section.builder()
                .name(request.getName())
                .semester(request.getSemester())
                .year(request.getYear())
                .rubric(rubric)
                .build();

        Section savedSection = sectionRepository.save(section);
        return toSectionResponse(savedSection);
    }

    public SectionResponse getSectionById(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + id));

        return toSectionResponse(section);
    }

    public List<SectionResponse> getAllSections() {
        return sectionRepository.findAll().stream()
                .map(this::toSectionResponse)
                .toList();
    }

    public SectionResponse updateSection(UpdateSectionRequest request) {
        Section section = sectionRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + request.getId()));

        Rubric rubric = rubricRepository.findById(request.getRubricId())
                .orElseThrow(() -> new RuntimeException("Rubric not found with id: " + request.getRubricId()));

        section.setName(request.getName());
        section.setSemester(request.getSemester());
        section.setYear(request.getYear());
        section.setRubric(rubric);

        Section savedSection = sectionRepository.save(section);
        return toSectionResponse(savedSection);
    }

    public List<ActiveWeekResponse> setActiveWeeks(SetActiveWeeksRequest request) {
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + request.getSectionId()));

        section.getActiveWeeks().clear();

        if (request.getWeeks() != null) {
            for (ActiveWeekRequest weekRequest : request.getWeeks()) {
                ActiveWeek activeWeek = ActiveWeek.builder()
                        .weekNumber(weekRequest.getWeekNumber())
                        .startDate(weekRequest.getStartDate())
                        .endDate(weekRequest.getEndDate())
                        .section(section)
                        .build();
                section.getActiveWeeks().add(activeWeek);
            }
        }

        Section savedSection = sectionRepository.save(section);
        return savedSection.getActiveWeeks().stream()
                .map(this::toActiveWeekResponse)
                .toList();
    }

    public List<ActiveWeekResponse> getActiveWeeks(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + sectionId));

        return section.getActiveWeeks().stream()
                .map(this::toActiveWeekResponse)
                .toList();
    }

    private SectionResponse toSectionResponse(Section section) {
        return SectionResponse.builder()
                .id(section.getId())
                .name(section.getName())
                .semester(section.getSemester())
                .year(section.getYear())
                .build();
    }

    private ActiveWeekResponse toActiveWeekResponse(ActiveWeek activeWeek) {
        return ActiveWeekResponse.builder()
                .id(activeWeek.getId())
                .weekNumber(activeWeek.getWeekNumber())
                .startDate(activeWeek.getStartDate())
                .endDate(activeWeek.getEndDate())
                .build();
    }
}
