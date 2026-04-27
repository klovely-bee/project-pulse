package com.projectpulse.backend.auth.service;

import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.user.domain.User;
import com.projectpulse.backend.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class SessionUserService {

    private static final String SESSION_USER_ID = "currentUserId";

    private final UserRepository userRepository;

    public SessionUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(HttpSession session, User user) {
        session.setAttribute(SESSION_USER_ID, user.getId());
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public User requireUser(HttpSession session) {
        Object userId = session.getAttribute(SESSION_USER_ID);
        if (!(userId instanceof Long id)) {
            throw new RuntimeException("Authentication required");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User account is inactive");
        }

        return user;
    }

    public User requireRole(HttpSession session, Role... allowedRoles) {
        User user = requireUser(session);
        boolean allowed = Arrays.stream(allowedRoles).anyMatch(role -> role == user.getRole());
        if (!allowed) {
            throw new RuntimeException("Forbidden");
        }
        return user;
    }

    public User requireSelfOrRole(HttpSession session, Long targetUserId, Role... allowedRoles) {
        User user = requireUser(session);
        if (user.getId().equals(targetUserId)) {
            return user;
        }

        boolean allowed = Arrays.stream(allowedRoles).anyMatch(role -> role == user.getRole());
        if (!allowed) {
            throw new RuntimeException("Forbidden");
        }
        return user;
    }
}
