package com.projectpulse.backend.student.service;

import com.projectpulse.backend.config.EmailService;
import com.projectpulse.backend.student.domain.Student;
import com.projectpulse.backend.student.dto.InviteStudentRequest;
import com.projectpulse.backend.student.repository.StudentRepository;
import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.user.domain.User;
import com.projectpulse.backend.user.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public List<Student> findStudents(String name) {
        return studentRepository.searchStudents(name.toLowerCase());
    }

    public Student inviteStudent(InviteStudentRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use: " + request.getEmail());
        }
        String[] nameParts = splitName(request.getName());
        User savedUser = userRepository.save(User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(nameParts[0])
                .lastName(nameParts[1])
                .role(Role.STUDENT)
                .active(true)
                .build());

        try {
            emailService.sendInviteEmail(request.getEmail(), request.getName(), request.getPassword());
        } catch (Exception e) {
            System.err.println("Failed to send invite email: " + e.getMessage());
        }

        return toStudent(savedUser);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findByIdAndRole(id, Role.STUDENT)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findByIdAndRole(id, Role.STUDENT)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        studentRepository.delete(student);
    }

    private Student toStudent(User user) {
        return Student.builder()
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
