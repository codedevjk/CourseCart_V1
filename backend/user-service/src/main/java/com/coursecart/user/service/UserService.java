package com.coursecart.user.service;

import com.coursecart.user.dto.CountResponse;
import com.coursecart.user.dto.UserLoginRequest;
import com.coursecart.user.dto.UserRegistrationRequest;
import com.coursecart.user.dto.UserResponse;
import com.coursecart.user.entity.Role;
import com.coursecart.user.entity.User;
import com.coursecart.user.exception.DuplicateResourceException;
import com.coursecart.user.exception.InvalidCredentialsException;
import com.coursecart.user.exception.ResourceNotFoundException;
import com.coursecart.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        
        User user = modelMapper.map(request, User.class);
        user.setRole(Role.USER);
        
        user = userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse login(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
                
        // Plain-string comparison for this educational capstone
        if (!user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse getUser(Long userId) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Fetch user by ID from repository. Throw ResourceNotFoundException if missing. Return mapped UserResponse (US 03).");
    }

    public CountResponse countUsers() {
        return new CountResponse(userRepository.count());
    }
}
