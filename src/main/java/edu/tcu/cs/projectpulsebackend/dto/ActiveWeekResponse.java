package edu.tcu.cs.projectpulsebackend.dto;

import java.time.LocalDate;

public class ActiveWeekResponse {

    private Long id;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private boolean active;
    private Long sectionId;

    public ActiveWeekResponse() {
    }

    public ActiveWeekResponse(Long id, LocalDate weekStartDate, LocalDate weekEndDate, boolean active, Long sectionId) {
        this.id = id;
        this.weekStartDate = weekStartDate;
        this.weekEndDate = weekEndDate;
        this.active = active;
        this.sectionId = sectionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }
}
