package com.whiletrue.onlineshop.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.whiletrue.onlineshop.exception.UserNotFoundException;
import com.whiletrue.onlineshop.model.User;
import com.whiletrue.onlineshop.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("yurii@example.com");
        user.setPassword("rawPassword");
    }

    @Test
    void testCreateUser() {
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("yurii@example.com", createdUser.getEmail());
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        when(userRepository.findByEmail("yurii@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPassword", user.getPassword())).thenReturn(true);

        User loggedInUser = userService.login("yurii@example.com", "rawPassword");

        assertNotNull(loggedInUser);
        assertEquals("yurii@example.com", loggedInUser.getEmail());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login("notfound@example.com", "wrongPassword"));
    }

    @Test
    void testLogin_InvalidPassword() {
        when(userRepository.findByEmail("yurii@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.login("yurii@example.com", "wrongPassword"));
    }

    @Test
    void testGetUserByEmail_UserExists() {
        when(userRepository.findByEmail("yurii@example.com")).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.getUserByEmail("yurii@example.com");

        assertTrue(retrievedUser.isPresent());
        assertEquals("yurii@example.com", retrievedUser.get().getEmail());
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Optional<User> retrievedUser = userService.getUserByEmail("unknown@example.com");

        assertFalse(retrievedUser.isPresent());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
