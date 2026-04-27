package com.projectpulse.backend.user.controller;

import com.projectpulse.backend.auth.service.SessionUserService;
import com.projectpulse.backend.user.dto.UserResponse;
import com.projectpulse.backend.user.service.UserService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.user.domain.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SessionUserService sessionUserService;

    public UserController(UserService userService, SessionUserService sessionUserService) {
        this.userService = userService;
        this.sessionUserService = sessionUserService;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireSelfOrRole(session, id, Role.ADMIN);
        return Result.success("User retrieved successfully", userService.findUserById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserResponse request, HttpSession session) {
        sessionUserService.requireSelfOrRole(session, id, Role.ADMIN);
        return Result.success("User updated successfully", userService.updateUser(id, request));
    }
}
