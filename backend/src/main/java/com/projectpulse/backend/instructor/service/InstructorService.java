package com.projectpulse.backend.instructor.service;

import com.projectpulse.backend.instructor.domain.Instructor;
import com.projectpulse.backend.instructor.repository.InstructorRepository;
import com.projectpulse.backend.user.domain.Role;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> findInstructors(String name) {
        return instructorRepository.searchInstructors(name.toLowerCase());
    }

    public Instructor getInstructorById(Long id) {
        return instructorRepository.findByIdAndRole(id, Role.INSTRUCTOR)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
    }

    public Instructor deactivateInstructor(Long id) {
        Instructor instructor = instructorRepository.findByIdAndRole(id, Role.INSTRUCTOR)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));

        instructor.setActive(false);
        return instructorRepository.save(instructor);
    }

    public Instructor reactivateInstructor(Long id) {
        Instructor instructor = instructorRepository.findByIdAndRole(id, Role.INSTRUCTOR)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));

        instructor.setActive(true);
        return instructorRepository.save(instructor);
    }
}
