package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.repository.UserDao;
import com.ideaas.ecomm.ecomm.services.UserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IAuthenticationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private IAuthenticationFacade authenticationFacade;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        when(userDao.getById("testuser")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userDao.getById("unknown")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("unknown"));
    }

    @Test
    void testSaveUser() {
        when(userDao.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals("testuser@example.com", savedUser.getUsername());
        assertNotEquals("password123", savedUser.getPassword()); // Contraseña debería estar encriptada
    }

    @Test
    void testUpdateUser() {
        when(userDao.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.update(user);

        assertNotNull(updatedUser);
        assertEquals("testuser@example.com", updatedUser.getEmail());
    }

    @Test
    void testGetUserExists() {
        when(userDao.findById("testuser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.get("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testGetUserNotExists() {
        when(userDao.findById("unknown")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.get("unknown");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindAllUsers() {
        List<User> users = Arrays.asList(user, new User());
        when(userDao.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();

        assertEquals(2, foundUsers.size());
    }

    @Test
    void testGetCurrentUser() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        when(authenticationFacade.getAuthentication()).thenReturn(auth);
        when(userDao.findById("testuser")).thenReturn(Optional.of(user));

        User currentUser = userService.getCurrent();

        assertNotNull(currentUser);
        assertEquals("testuser", currentUser.getUsername());
    }

    @Test
    void testGetCurrentUser_NotFound() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("unknown");
        when(authenticationFacade.getAuthentication()).thenReturn(auth);
        when(userDao.findById("unknown")).thenReturn(Optional.empty());

        User currentUser = userService.getCurrent();

        assertNull(currentUser);
    }
}