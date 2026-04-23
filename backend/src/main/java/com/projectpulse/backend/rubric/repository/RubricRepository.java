package com.projectpulse.backend.rubric.repository;

import com.projectpulse.backend.rubric.domain.Rubric;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RubricRepository extends JpaRepository<Rubric, Long> {

    Optional<Rubric> findByName(String name);
}
