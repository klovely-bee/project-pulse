package com.projectpulse.backend.war.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWarRequest {

    private Long userId;
    private Long teamId;
    private Integer weekNumber;
    private String content;
}
