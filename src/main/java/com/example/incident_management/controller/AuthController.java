package com.example.incident_management.controller;

import com.example.incident_management.entity.LoginRequest;
import com.example.incident_management.security.JwtService;
import com.example.incident_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest) {

        var user =
                userService.findByUserName(loginRequest.getUserName());

        // Check whether user exists
        if (user == null) {
            return new ResponseEntity<>(
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED
            );
        }

        // Check password
        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {

            return new ResponseEntity<>(
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED
            );
        }

        // Generate JWT
        String token =
                jwtService.generateToken(user.getUserName());

        return new ResponseEntity<>(
                Map.of(
                        "token", token,
                        "userName", user.getUserName()
                ),
                HttpStatus.OK
        );
    }
}

