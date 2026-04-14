package edu.tcu.cs.projectpulsebackend.dto;

import java.time.LocalDate;

public class SectionResponse {

    private Long id;
    private String sectionName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long rubricId;
    private String rubricName;

    public SectionResponse() {
    }

    public SectionResponse(Long id, String sectionName, LocalDate startDate, LocalDate endDate,
            Long rubricId, String rubricName) {
        this.id = id;
        this.sectionName = sectionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rubricId = rubricId;
        this.rubricName = rubricName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getRubricId() {
        return rubricId;
    }

    public void setRubricId(Long rubricId) {
        this.rubricId = rubricId;
    }

    public String getRubricName() {
        return rubricName;
    }

    public void setRubricName(String rubricName) {
        this.rubricName = rubricName;
    }
}
