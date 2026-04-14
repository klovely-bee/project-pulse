package edu.tcu.cs.projectpulsebackend.dto;

import java.util.List;

public class RubricResponse {

    private Long id;
    private String name;
    private List<CriterionResponse> criteria;

    public RubricResponse() {
    }

    public RubricResponse(Long id, String name, List<CriterionResponse> criteria) {
        this.id = id;
        this.name = name;
        this.criteria = criteria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CriterionResponse> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<CriterionResponse> criteria) {
        this.criteria = criteria;
    }
}
