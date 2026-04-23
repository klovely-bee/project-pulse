package com.projectpulse.backend.week.repository;

import com.projectpulse.backend.week.domain.ActiveWeek;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveWeekRepository extends JpaRepository<ActiveWeek, Long> {

    List<ActiveWeek> findBySectionId(Long sectionId);
}
