package edu.tcu.cs.projectpulsebackend.repository;

import edu.tcu.cs.projectpulsebackend.model.Section;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    boolean existsBySectionName(String sectionName);

    Optional<Section> findBySectionName(String sectionName);
}
