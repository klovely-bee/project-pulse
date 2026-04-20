package edu.tcu.cs.projectpulsebackend.service;

import edu.tcu.cs.projectpulsebackend.dto.ActiveWeekResponse;
import edu.tcu.cs.projectpulsebackend.dto.ActiveWeekSetupRequest;
import edu.tcu.cs.projectpulsebackend.model.ActiveWeek;
import edu.tcu.cs.projectpulsebackend.model.Section;
import edu.tcu.cs.projectpulsebackend.repository.ActiveWeekRepository;
import edu.tcu.cs.projectpulsebackend.repository.SectionRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActiveWeekService {

    private final ActiveWeekRepository activeWeekRepository;
    private final SectionRepository sectionRepository;

    public ActiveWeekService(ActiveWeekRepository activeWeekRepository, SectionRepository sectionRepository) {
        this.activeWeekRepository = activeWeekRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public List<ActiveWeekResponse> getWeeksForSection(Long sectionId) {
        Section section = getSectionById(sectionId);
        List<ActiveWeek> savedWeeks = activeWeekRepository.findBySectionIdOrderByWeekStartDateAsc(sectionId);
        if (!savedWeeks.isEmpty()) {
            return savedWeeks.stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        return buildWeeks(section, Set.of()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public List<ActiveWeekResponse> setupActiveWeeks(Long sectionId, ActiveWeekSetupRequest request) {
        Section section = getSectionById(sectionId);
        Set<LocalDate> inactiveWeekStartDates = extractInactiveWeekStartDates(request);
        List<ActiveWeek> validWeeks = buildWeeks(section, inactiveWeekStartDates);

        Set<LocalDate> validWeekStartDates = validWeeks.stream()
                .map(ActiveWeek::getWeekStartDate)
                .collect(java.util.stream.Collectors.toSet());

        for (LocalDate inactiveWeekStartDate : inactiveWeekStartDates) {
            if (!validWeekStartDates.contains(inactiveWeekStartDate)) {
                throw new RuntimeException("Invalid inactive week start date for section: " + inactiveWeekStartDate);
            }
        }

        activeWeekRepository.deleteBySectionId(sectionId);
        List<ActiveWeek> savedWeeks = activeWeekRepository.saveAll(validWeeks);

        return savedWeeks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Section getSectionById(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + sectionId));
    }

    private Set<LocalDate> extractInactiveWeekStartDates(ActiveWeekSetupRequest request) {
        if (request == null || request.getInactiveWeekStartDates() == null) {
            return Set.of();
        }
        return new HashSet<>(request.getInactiveWeekStartDates());
    }

    private List<ActiveWeek> buildWeeks(Section section, Set<LocalDate> inactiveWeekStartDates) {
        LocalDate currentWeekStart = alignToMonday(section.getStartDate());
        LocalDate sectionEndDate = section.getEndDate();
        java.util.ArrayList<ActiveWeek> weeks = new java.util.ArrayList<>();

        while (!currentWeekStart.isAfter(sectionEndDate)) {
            LocalDate currentWeekEnd = currentWeekStart.plusDays(6);
            boolean overlapsSection = !currentWeekEnd.isBefore(section.getStartDate())
                    && !currentWeekStart.isAfter(sectionEndDate);

            if (overlapsSection) {
                boolean active = !inactiveWeekStartDates.contains(currentWeekStart);
                weeks.add(new ActiveWeek(currentWeekStart, currentWeekEnd, active, section));
            }

            currentWeekStart = currentWeekStart.plusWeeks(1);
        }

        return weeks;
    }

    private LocalDate alignToMonday(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    private ActiveWeekResponse mapToResponse(ActiveWeek activeWeek) {
        Long sectionId = activeWeek.getSection() != null ? activeWeek.getSection().getId() : null;
        return new ActiveWeekResponse(
                activeWeek.getId(),
                activeWeek.getWeekStartDate(),
                activeWeek.getWeekEndDate(),
                activeWeek.isActive(),
                sectionId
        );
    }
}
