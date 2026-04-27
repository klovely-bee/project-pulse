package com.projectpulse.backend.instructor.service;

import com.projectpulse.backend.instructor.domain.Instructor;
import com.projectpulse.backend.instructor.dto.InviteInstructorRequest;
import com.projectpulse.backend.instructor.repository.InstructorRepository;
import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.user.domain.User;
import com.projectpulse.backend.user.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public InstructorService(InstructorRepository instructorRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.instructorRepository = instructorRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Instructor> findInstructors(String name) {
        return instructorRepository.searchInstructors(name.toLowerCase());
    }

    public Instructor inviteInstructor(InviteInstructorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use: " + request.getEmail());
        }

        String[] nameParts = splitName(request.getName());
        User savedUser = userRepository.save(User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(nameParts[0])
                .lastName(nameParts[1])
                .role(Role.INSTRUCTOR)
                .active(true)
                .build());

        return toInstructor(savedUser);
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

    private Instructor toInstructor(User user) {
        return Instructor.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }

    private String[] splitName(String name) {
        String trimmedName = name == null ? "" : name.trim();
        if (trimmedName.isEmpty()) {
            throw new RuntimeException("Name is required");
        }

        String[] parts = trimmedName.split("\\s+", 2);
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "";
        return new String[]{firstName, lastName};
    }
}
