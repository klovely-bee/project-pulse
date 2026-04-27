package com.projectpulse.backend.instructor.controller;

import com.projectpulse.backend.auth.service.SessionUserService;
import com.projectpulse.backend.instructor.domain.Instructor;
import com.projectpulse.backend.instructor.dto.InviteInstructorRequest;
import com.projectpulse.backend.instructor.service.InstructorService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.user.domain.Role;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;
    private final SessionUserService sessionUserService;

    public InstructorController(InstructorService instructorService, SessionUserService sessionUserService) {
        this.instructorService = instructorService;
        this.sessionUserService = sessionUserService;
    }

    @GetMapping
    public ApiResponse<List<Instructor>> findInstructors(@RequestParam(required = false) String name, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        List<Instructor> instructors = (name == null || name.isBlank())
                ? instructorService.findInstructors("")
                : instructorService.findInstructors(name);

        return Result.success("Instructors retrieved", instructors);
    }

    @PostMapping
    public ApiResponse<Instructor> inviteInstructor(@RequestBody InviteInstructorRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Instructor invited successfully", instructorService.inviteInstructor(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<Instructor> getInstructorById(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Instructor retrieved", instructorService.getInstructorById(id));
    }

    @PutMapping("/{id}/deactivate")
    public ApiResponse<Instructor> deactivateInstructor(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Instructor deactivated", instructorService.deactivateInstructor(id));
    }

    @PutMapping("/{id}/reactivate")
    public ApiResponse<Instructor> reactivateInstructor(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Instructor reactivated", instructorService.reactivateInstructor(id));
    }
}
