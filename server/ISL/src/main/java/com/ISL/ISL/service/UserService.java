package com.ISL.ISL.service;

import com.ISL.ISL.dto.UserRequest;
import com.ISL.ISL.dto.UserResponse;
import com.ISL.ISL.model.AuthProvider;
import com.ISL.ISL.model.Role;
import com.ISL.ISL.model.User;
import com.ISL.ISL.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPreferredLanguage(userRequest.getPreferredLanguage());
        user.setRole(Role.ROLE_USER);
        user.setProvider(AuthProvider.LOCAL);

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }


    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setRole(user.getRole());
        response.setProvider(user.getProvider());
        response.setProviderId(user.getProviderId());
        response.setPreferredLanguage(user.getPreferredLanguage());
        return response;
    }
}
