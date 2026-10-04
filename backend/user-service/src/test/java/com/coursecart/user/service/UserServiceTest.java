package com.coursecart.user.service;

import com.coursecart.user.dto.CountResponse;
import com.coursecart.user.dto.UserLoginRequest;
import com.coursecart.user.dto.UserRegistrationRequest;
import com.coursecart.user.dto.UserResponse;
import com.coursecart.user.entity.Role;
import com.coursecart.user.entity.User;
import com.coursecart.user.exception.UserServiceException;
import com.coursecart.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Jane Doe", "jane_doe", "password123", Role.USER);
        testUser.setId(1L);
    }

    @Test
    void testRegister_Success() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Jane Doe");
        request.setUsername("jane_doe");
        request.setPassword("password123");

        when(userRepository.existsByUsername("jane_doe")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        when(modelMapper.map(any(UserRegistrationRequest.class), eq(User.class))).thenReturn(testUser);
        
        UserResponse mockResponse = new UserResponse();
        mockResponse.setId(1L);
        mockResponse.setUsername("jane_doe");
        mockResponse.setRole("USER");
        
        when(modelMapper.map(any(User.class), eq(UserResponse.class))).thenReturn(mockResponse);

        UserResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("jane_doe", response.getUsername());
        assertEquals("USER", response.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_DuplicateUsername() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("jane_doe");

        when(userRepository.existsByUsername("jane_doe")).thenReturn(true);

        assertThrows(UserServiceException.class, () -> {
            userService.register(request);
        });
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("jane_doe");
        request.setPassword("password123");

        when(userRepository.findByUsername("jane_doe")).thenReturn(Optional.of(testUser));
        
        UserResponse mockResponse = new UserResponse();
        mockResponse.setId(1L);
        mockResponse.setUsername("jane_doe");
        
        when(modelMapper.map(any(User.class), eq(UserResponse.class))).thenReturn(mockResponse);

        UserResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("jane_doe", response.getUsername());
    }

    @Test
    void testLogin_InvalidPassword() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("jane_doe");
        request.setPassword("wrong_password");

        when(userRepository.findByUsername("jane_doe")).thenReturn(Optional.of(testUser));

        assertThrows(UserServiceException.class, () -> {
            userService.login(request);
        });
    }

    @Test
    void testLogin_UserNotFound() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("unknown_user");
        request.setPassword("password123");

        when(userRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());

        assertThrows(UserServiceException.class, () -> {
            userService.login(request);
        });
    }

    @Test
    void testCountUsers() {
        when(userRepository.count()).thenReturn(145L);
        CountResponse response = userService.countUsers();
        assertEquals(145L, response.getTotalUsers());
    }

    @Test
    void testGetUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        UserResponse mockResponse = new UserResponse();
        mockResponse.setId(1L);
        mockResponse.setUsername("jane_doe");
        
        when(modelMapper.map(any(User.class), eq(UserResponse.class))).thenReturn(mockResponse);
        
        UserResponse response = userService.getUser(1L);
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("jane_doe", response.getUsername());
    }

    @Test
    void testGetUser_NotFound_ThrowsResourceNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserServiceException.class, () -> {
            userService.getUser(99L);
        });
    }
}
