package com.projectpulse.backend.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.projectpulse.backend.auth.dto.InstructorRegistrationRequest;
import com.projectpulse.backend.auth.dto.LoginRequest;
import com.projectpulse.backend.auth.dto.LoginResponse;
import com.projectpulse.backend.auth.dto.StudentRegistrationRequest;
import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.user.domain.User;
import com.projectpulse.backend.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterStudent_WhenEmailIsNew() {
        StudentRegistrationRequest request = StudentRegistrationRequest.builder()
                .email("student1@tcu.edu")
                .password("Password123!")
                .firstName("Ava")
                .lastName("Student")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .email(request.getEmail())
                .password("encoded-password")
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.STUDENT)
                .active(true)
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        LoginResponse response = authService.registerStudent(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User userToSave = userCaptor.getValue();
        assertEquals(Role.STUDENT, userToSave.getRole());
        assertEquals("encoded-password", userToSave.getPassword());
        assertEquals("student1@tcu.edu", response.getEmail());
        assertEquals(Role.STUDENT, response.getRole());
    }

    @Test
    void shouldRegisterInstructor_WhenEmailIsNew() {
        InstructorRegistrationRequest request = InstructorRegistrationRequest.builder()
                .email("instructor1@tcu.edu")
                .password("Password123!")
                .firstName("Iris")
                .lastName("Instructor")
                .build();

        User savedUser = User.builder()
                .id(2L)
                .email(request.getEmail())
                .password("encoded-password")
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.INSTRUCTOR)
                .active(true)
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        LoginResponse response = authService.registerInstructor(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User userToSave = userCaptor.getValue();
        assertEquals(Role.INSTRUCTOR, userToSave.getRole());
        assertEquals("encoded-password", userToSave.getPassword());
        assertEquals("instructor1@tcu.edu", response.getEmail());
        assertEquals(Role.INSTRUCTOR, response.getRole());
    }

    @Test
    void shouldThrowException_WhenRegisteringStudentWithExistingEmail() {
        StudentRegistrationRequest request = StudentRegistrationRequest.builder()
                .email("student1@tcu.edu")
                .password("Password123!")
                .firstName("Ava")
                .lastName("Student")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.registerStudent(request));

        assertEquals("Email is already in use: student1@tcu.edu", exception.getMessage());
    }

    @Test
    void shouldLoginSuccessfully_WhenCredentialsAreValid() {
        LoginRequest request = LoginRequest.builder()
                .email("student1@tcu.edu")
                .password("Password123!")
                .build();

        User user = User.builder()
                .id(1L)
                .email("student1@tcu.edu")
                .password("encoded-password")
                .firstName("Ava")
                .lastName("Student")
                .role(Role.STUDENT)
                .active(true)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        LoginResponse response = authService.login(request);

        assertEquals(1L, response.getId());
        assertEquals("student1@tcu.edu", response.getEmail());
        assertEquals(Role.STUDENT, response.getRole());
    }

    @Test
    void shouldThrowException_WhenLoginEmailDoesNotExist() {
        LoginRequest request = LoginRequest.builder()
                .email("missing@tcu.edu")
                .password("Password123!")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("User not found with email: missing@tcu.edu", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenPasswordIsInvalid() {
        LoginRequest request = LoginRequest.builder()
                .email("student1@tcu.edu")
                .password("WrongPassword!")
                .build();

        User user = User.builder()
                .id(1L)
                .email("student1@tcu.edu")
                .password("encoded-password")
                .firstName("Ava")
                .lastName("Student")
                .role(Role.STUDENT)
                .active(true)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Invalid email or password", exception.getMessage());
    }
}
