package com.projectpulse.backend.week.dto;

import java.time.LocalDate;
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
public class ActiveWeekResponse {

    private Long id;
    private Integer weekNumber;
    private LocalDate startDate;
    private LocalDate endDate;
}
