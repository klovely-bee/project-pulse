package edu.tcu.cs.projectpulsebackend.repository;

import edu.tcu.cs.projectpulsebackend.model.ActiveWeek;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveWeekRepository extends JpaRepository<ActiveWeek, Long> {

    List<ActiveWeek> findBySectionIdOrderByWeekStartDateAsc(Long sectionId);

    void deleteBySectionId(Long sectionId);
}
