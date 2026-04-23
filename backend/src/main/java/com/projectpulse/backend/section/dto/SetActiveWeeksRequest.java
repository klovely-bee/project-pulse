package com.projectpulse.backend.section.dto;

import com.projectpulse.backend.week.dto.ActiveWeekRequest;
import java.util.List;
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
public class SetActiveWeeksRequest {

    private Long sectionId;
    private List<ActiveWeekRequest> weeks;
}
