package com.projectpulse.backend.student.controller;

import com.projectpulse.backend.auth.service.SessionUserService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.student.domain.Student;
import com.projectpulse.backend.student.dto.InviteStudentRequest;
import com.projectpulse.backend.student.service.StudentService;
import com.projectpulse.backend.user.domain.Role;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final SessionUserService sessionUserService;

    public StudentController(StudentService studentService, SessionUserService sessionUserService) {
        this.studentService = studentService;
        this.sessionUserService = sessionUserService;
    }

    @GetMapping
    public ApiResponse<List<Student>> findStudents(@RequestParam(required = false) String name, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN, Role.INSTRUCTOR);
        List<Student> students = (name == null || name.isBlank())
                ? studentService.findStudents("")
                : studentService.findStudents(name);

        return Result.success("Students retrieved", students);
    }

    @PostMapping
    public ApiResponse<Student> inviteStudent(@RequestBody InviteStudentRequest request, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        return Result.success("Student invited successfully", studentService.inviteStudent(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<Student> getStudentById(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN, Role.INSTRUCTOR);
        return Result.success("Student retrieved", studentService.getStudentById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStudent(@PathVariable Long id, HttpSession session) {
        sessionUserService.requireRole(session, Role.ADMIN);
        studentService.deleteStudent(id);
        return Result.success("Student deleted");
    }
}
