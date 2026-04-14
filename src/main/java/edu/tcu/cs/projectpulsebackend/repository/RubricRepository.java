package edu.tcu.cs.projectpulsebackend.repository;

import edu.tcu.cs.projectpulsebackend.model.Rubric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RubricRepository extends JpaRepository<Rubric, Long> {

    boolean existsByName(String name);
}
