package com.projectpulse.backend.team.repository;

import com.projectpulse.backend.team.domain.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findBySectionId(Long sectionId);
}
