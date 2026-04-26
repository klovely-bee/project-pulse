package com.projectpulse.backend.student.repository;

import com.projectpulse.backend.student.domain.Student;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("""
    SELECT s FROM Student s
    WHERE s.role = com.projectpulse.backend.user.domain.Role.STUDENT
      AND (
           LOWER(s.firstName) LIKE %:name%
        OR LOWER(s.lastName) LIKE %:name%
        OR LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE %:name%
        OR LOWER(s.email) LIKE %:name%
      )
    """)
    List<Student> searchStudents(@Param("name") String name);
}
