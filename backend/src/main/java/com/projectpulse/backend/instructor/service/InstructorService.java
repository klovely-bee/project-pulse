package com.projectpulse.backend.instructor.service;

import com.projectpulse.backend.instructor.domain.Instructor;
import com.projectpulse.backend.instructor.repository.InstructorRepository;
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
}
