package edu.tcu.cs.projectpulsebackend.service;

import edu.tcu.cs.projectpulsebackend.dto.SectionCreateRequest;
import edu.tcu.cs.projectpulsebackend.dto.SectionResponse;
import edu.tcu.cs.projectpulsebackend.dto.SectionUpdateRequest;
import edu.tcu.cs.projectpulsebackend.model.Rubric;
import edu.tcu.cs.projectpulsebackend.model.Section;
import edu.tcu.cs.projectpulsebackend.repository.RubricRepository;
import edu.tcu.cs.projectpulsebackend.repository.SectionRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final RubricRepository rubricRepository;

    public SectionService(SectionRepository sectionRepository, RubricRepository rubricRepository) {
        this.sectionRepository = sectionRepository;
        this.rubricRepository = rubricRepository;
    }

    @Transactional
    public SectionResponse createSection(SectionCreateRequest request) {
        if (request == null) {
            throw new RuntimeException("Section request is required.");
        }
        if (isBlank(request.getSectionName())) {
            throw new RuntimeException("Section name is required.");
        }

        String sectionName = request.getSectionName().trim();
        if (sectionRepository.existsBySectionName(sectionName)) {
            throw new RuntimeException("A section with this name already exists.");
        }

        validateDates(request.getStartDate(), request.getEndDate());
        Rubric rubric = getRubricById(request.getRubricId());

        Section section = new Section(sectionName, request.getStartDate(), request.getEndDate(), rubric);
        Section savedSection = sectionRepository.save(section);
        return mapToResponse(savedSection);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findSections() {
        return sectionRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SectionResponse getSectionById(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + id));
        return mapToResponse(section);
    }

    @Transactional
    public SectionResponse updateSection(Long id, SectionUpdateRequest request) {
        if (request == null) {
            throw new RuntimeException("Section update request is required.");
        }
        if (isBlank(request.getSectionName())) {
            throw new RuntimeException("Section name is required.");
        }

        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + id));

        String sectionName = request.getSectionName().trim();
        if (!section.getSectionName().equals(sectionName) && sectionRepository.existsBySectionName(sectionName)) {
            throw new RuntimeException("A section with this name already exists.");
        }

        validateDates(request.getStartDate(), request.getEndDate());
        Rubric rubric = getRubricById(request.getRubricId());

        section.setSectionName(sectionName);
        section.setStartDate(request.getStartDate());
        section.setEndDate(request.getEndDate());
        section.setRubric(rubric);

        Section updatedSection = sectionRepository.save(section);
        return mapToResponse(updatedSection);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new RuntimeException("Start date and end date are required.");
        }
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Start date cannot be after end date.");
        }
    }

    private Rubric getRubricById(Long rubricId) {
        if (rubricId == null) {
            throw new RuntimeException("Rubric id is required.");
        }
        return rubricRepository.findById(rubricId)
                .orElseThrow(() -> new RuntimeException("Rubric not found with id: " + rubricId));
    }

    private SectionResponse mapToResponse(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getSectionName(),
                section.getStartDate(),
                section.getEndDate(),
                section.getRubric().getId(),
                section.getRubric().getName()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
