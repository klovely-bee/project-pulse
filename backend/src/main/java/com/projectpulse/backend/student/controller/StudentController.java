package com.projectpulse.backend.student.controller;

import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import com.projectpulse.backend.student.domain.Student;
import com.projectpulse.backend.student.service.StudentService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ApiResponse<List<Student>> findStudents(@RequestParam(required = false) String name) {
        List<Student> students = (name == null || name.isBlank())
                ? studentService.findStudents("")
                : studentService.findStudents(name);

        return Result.success("Students retrieved", students);
    }
}
