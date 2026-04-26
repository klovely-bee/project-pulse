package com.projectpulse.backend.instructor.repository;

import com.projectpulse.backend.instructor.domain.Instructor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    @Query("""
    SELECT i FROM Instructor i
    WHERE i.role = com.projectpulse.backend.user.domain.Role.INSTRUCTOR
      AND (
           LOWER(i.firstName) LIKE %:name%
        OR LOWER(i.lastName) LIKE %:name%
        OR LOWER(CONCAT(i.firstName, ' ', i.lastName)) LIKE %:name%
        OR LOWER(i.email) LIKE %:name%
      )
    """)
    List<Instructor> searchInstructors(@Param("name") String name);
}
