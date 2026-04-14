package edu.tcu.cs.projectpulsebackend.dto;

public class CriterionResponse {

    private Long id;
    private String name;
    private String description;
    private Double maxScore;

    public CriterionResponse() {
    }

    public CriterionResponse(Long id, String name, String description, Double maxScore) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxScore = maxScore;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }
}
