package com.projectpulse.backend.section.repository;

import com.projectpulse.backend.section.domain.Section;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByName(String name);
}
