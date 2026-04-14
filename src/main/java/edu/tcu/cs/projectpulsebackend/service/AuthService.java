package edu.tcu.cs.projectpulsebackend.service;

import edu.tcu.cs.projectpulsebackend.dto.AuthResponse;
import edu.tcu.cs.projectpulsebackend.dto.LoginRequest;
import edu.tcu.cs.projectpulsebackend.dto.RegisterRequest;
import edu.tcu.cs.projectpulsebackend.model.Role;
import edu.tcu.cs.projectpulsebackend.model.User;
import edu.tcu.cs.projectpulsebackend.repository.UserRepository;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        if (request == null) {
            throw new RuntimeException("Register request is required.");
        }
        if (isBlank(request.getName()) || isBlank(request.getEmail()) || isBlank(request.getPassword()) || isBlank(request.getRole())) {
            throw new RuntimeException("Name, email, password, and role are required.");
        }
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new RuntimeException("An account with this email already exists.");
        }

        Role role = parseRole(request.getRole());
        User user = new User(
                request.getName().trim(),
                request.getEmail().trim(),
                passwordEncoder.encode(request.getPassword()),
                role,
                true
        );
        userRepository.save(user);

        return new AuthResponse("User registered successfully.", role.name());
    }

    public AuthResponse login(LoginRequest request) {
        if (request == null) {
            throw new RuntimeException("Login request is required.");
        }
        if (isBlank(request.getEmail()) || isBlank(request.getPassword())) {
            throw new RuntimeException("Email and password are required.");
        }

        User user = userRepository.findByEmail(request.getEmail().trim())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        if (!user.isActive()) {
            throw new RuntimeException("This account is inactive.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        return new AuthResponse("Login successful.", user.getRole().name());
    }

    private Role parseRole(String roleValue) {
        try {
            return Role.valueOf(roleValue.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid role. Allowed roles are ADMIN, STUDENT, and INSTRUCTOR.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
