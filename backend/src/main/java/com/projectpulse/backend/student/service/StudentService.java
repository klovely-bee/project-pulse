package com.projectpulse.backend.student.service;

import com.projectpulse.backend.student.domain.Student;
import com.projectpulse.backend.student.repository.StudentRepository;
import com.projectpulse.backend.user.domain.Role;
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

    public Student getStudentById(Long id) {
        return studentRepository.findByIdAndRole(id, Role.STUDENT)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findByIdAndRole(id, Role.STUDENT)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        studentRepository.delete(student);
    }
}
