package com.projectpulse.backend.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.projectpulse.backend.user.domain.Role;
import com.projectpulse.backend.user.domain.User;
import com.projectpulse.backend.user.dto.UserResponse;
import com.projectpulse.backend.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldFindUserById_WhenUserExists() {
        User user = User.builder()
                .id(1L)
                .email("student1@tcu.edu")
                .password("encoded-password")
                .firstName("Ava")
                .lastName("Student")
                .role(Role.STUDENT)
                .active(true)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.findUserById(1L);

        assertEquals(1L, response.getId());
        assertEquals("student1@tcu.edu", response.getEmail());
        assertEquals("Ava", response.getFirstName());
        assertEquals("Student", response.getLastName());
        assertEquals(Role.STUDENT, response.getRole());
        assertTrue(response.isActive());
    }

    @Test
    void shouldThrowException_WhenFindingUserByIdThatDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.findUserById(99L));

        assertEquals("User not found with id: 99", exception.getMessage());
    }

    @Test
    void shouldUpdateUser_WhenUserExists() {
        User existingUser = User.builder()
                .id(1L)
                .email("student1@tcu.edu")
                .password("encoded-password")
                .firstName("Ava")
                .lastName("Student")
                .role(Role.STUDENT)
                .active(true)
                .build();

        User savedUser = User.builder()
                .id(1L)
                .email("student1.updated@tcu.edu")
                .password("encoded-password")
                .firstName("AvaUpdated")
                .lastName("StudentUpdated")
                .role(Role.STUDENT)
                .active(true)
                .build();

        UserResponse request = UserResponse.builder()
                .email("student1.updated@tcu.edu")
                .firstName("AvaUpdated")
                .lastName("StudentUpdated")
                .role(Role.STUDENT)
                .active(true)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.updateUser(1L, request);

        assertEquals("student1.updated@tcu.edu", response.getEmail());
        assertEquals("AvaUpdated", response.getFirstName());
        assertEquals("StudentUpdated", response.getLastName());
        assertEquals(Role.STUDENT, response.getRole());
        assertTrue(response.isActive());
    }

    @Test
    void shouldThrowException_WhenUpdatingUserThatDoesNotExist() {
        UserResponse request = UserResponse.builder()
                .email("student1.updated@tcu.edu")
                .firstName("AvaUpdated")
                .lastName("StudentUpdated")
                .role(Role.STUDENT)
                .active(true)
                .build();

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(99L, request));

        assertEquals("User not found with id: 99", exception.getMessage());
    }

    @Test
    void shouldMapUserToUserResponse() {
        User user = User.builder()
                .id(10L)
                .email("instructor1@tcu.edu")
                .password("encoded-password")
                .firstName("Iris")
                .lastName("Instructor")
                .role(Role.INSTRUCTOR)
                .active(false)
                .build();

        UserResponse response = userService.toUserResponse(user);

        assertEquals(10L, response.getId());
        assertEquals("instructor1@tcu.edu", response.getEmail());
        assertEquals("Iris", response.getFirstName());
        assertEquals("Instructor", response.getLastName());
        assertEquals(Role.INSTRUCTOR, response.getRole());
        assertFalse(response.isActive());
    }
}
