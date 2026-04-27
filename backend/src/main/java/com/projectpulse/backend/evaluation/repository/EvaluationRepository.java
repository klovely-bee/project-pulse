package com.projectpulse.backend.evaluation.repository;

import com.projectpulse.backend.evaluation.domain.Evaluation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByEvaluatorIdOrderByCreatedAtDesc(Long evaluatorId);

    List<Evaluation> findByTeamIdOrderByCreatedAtDesc(Long teamId);

    List<Evaluation> findByEvaluateeIdOrderByCreatedAtDesc(Long evaluateeId);
}
