package com.projectpulse.backend.student.service;

import com.projectpulse.backend.student.domain.Student;
import com.projectpulse.backend.student.repository.StudentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findStudents(String name) {
        return studentRepository.searchStudents(name.toLowerCase());
    }
}
