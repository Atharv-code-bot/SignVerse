package com.ISL.ISL.controller;


import com.ISL.ISL.config.JwtUtil;
import com.ISL.ISL.dto.AuthResponse;
import com.ISL.ISL.dto.LoginRequest;
import com.ISL.ISL.dto.UserRequest;
import com.ISL.ISL.dto.UserResponse;
import com.ISL.ISL.model.AuthProvider;
import com.ISL.ISL.model.User;
import com.ISL.ISL.repository.UserRepository;
import com.ISL.ISL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
        try {
            UserResponse response = userService.createUser(userRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Register New User
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
//        try {
//            // Create and save the new user
//            UserResponse response = userService.createUser(userRequest);
//
//            // ✅ Authenticate the user
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword())
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // ✅ Generate JWT Token
//            String token = jwtUtil.generateToken(userRequest.getEmail());
//
//            // ✅ Return both user details and token
//            return ResponseEntity.ok(new AuthResponse(token));
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


    // ✅ Login User
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found.");
            }

            User user = userOptional.get();

            // Ensure OAuth2 users have set a password before allowing login
            if (user.getProvider() == AuthProvider.GOOGLE && (user.getPassword() == null || user.getPassword().isEmpty())) {
                return ResponseEntity.badRequest().body("You signed up with Google. Please set a password first.");
            }

            // ✅ Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ✅ Generate JWT Token
            String token = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }



}

