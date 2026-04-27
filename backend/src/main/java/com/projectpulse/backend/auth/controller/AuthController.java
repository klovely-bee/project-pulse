package com.projectpulse.backend.auth.controller;

import com.projectpulse.backend.auth.dto.InstructorRegistrationRequest;
import com.projectpulse.backend.auth.dto.LoginRequest;
import com.projectpulse.backend.auth.dto.LoginResponse;
import com.projectpulse.backend.auth.dto.StudentRegistrationRequest;
import com.projectpulse.backend.auth.service.AuthService;
import com.projectpulse.backend.auth.service.SessionUserService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import jakarta.servlet.http.HttpSession;
import com.projectpulse.backend.user.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final SessionUserService sessionUserService;

    public AuthController(AuthService authService, SessionUserService sessionUserService) {
        this.authService = authService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping("/register/student")
    public ApiResponse<LoginResponse> registerStudent(@RequestBody StudentRegistrationRequest request) {
        return Result.success("Student registered successfully", authService.registerStudent(request));
    }

    @PostMapping("/register/instructor")
    public ApiResponse<LoginResponse> registerInstructor(@RequestBody InstructorRegistrationRequest request) {
        return Result.success("Instructor registered successfully", authService.registerInstructor(request));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = authService.login(request);
        User user = authService.findActiveUserByEmail(request.getEmail());
        sessionUserService.login(session, user);
        return Result.success("Login successful", response);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        sessionUserService.logout(session);
        return Result.success("Logout successful");
    }
}
