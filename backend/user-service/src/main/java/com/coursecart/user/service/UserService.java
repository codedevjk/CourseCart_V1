package com.coursecart.user.service;

import com.coursecart.user.dto.CountResponse;
import com.coursecart.user.dto.UserLoginRequest;
import com.coursecart.user.dto.UserRegistrationRequest;
import com.coursecart.user.dto.UserResponse;
import com.coursecart.user.entity.Role;
import com.coursecart.user.entity.User;
import com.coursecart.user.exception.ErrorMessages;
import com.coursecart.user.exception.UserServiceException;
import com.coursecart.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
            throw new UserServiceException(HttpStatus.CONFLICT, ErrorMessages.USERNAME_EXISTS);
        }
        
        User user = modelMapper.map(request, User.class);
        user.setRole(Role.USER);
        
        user = userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse login(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserServiceException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_CREDENTIALS));
                
        // Plain-string comparison for this educational capstone
        if (!user.getPassword().equals(request.getPassword())) {
            throw new UserServiceException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_CREDENTIALS);
        }
        
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserServiceException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND));
        return modelMapper.map(user, UserResponse.class);
    }

    public CountResponse countUsers() {
        return new CountResponse(userRepository.count());
    }
}
