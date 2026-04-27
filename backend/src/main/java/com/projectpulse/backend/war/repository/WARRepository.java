package com.projectpulse.backend.war.repository;

import com.projectpulse.backend.war.domain.WAR;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WARRepository extends JpaRepository<WAR, Long> {

    List<WAR> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<WAR> findByTeamIdOrderByCreatedAtDesc(Long teamId);
}
