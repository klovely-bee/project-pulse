package com.projectpulse.backend.rubric.repository;

import com.projectpulse.backend.rubric.domain.RubricCriterion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RubricCriterionRepository extends JpaRepository<RubricCriterion, Long> {

    List<RubricCriterion> findByRubricId(Long rubricId);
}
