package edu.tcu.cs.projectpulsebackend.dto;

import java.util.List;

public class RubricCreateRequest {

    private String name;
    private List<CriterionRequest> criteria;

    public RubricCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CriterionRequest> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<CriterionRequest> criteria) {
        this.criteria = criteria;
    }
}
