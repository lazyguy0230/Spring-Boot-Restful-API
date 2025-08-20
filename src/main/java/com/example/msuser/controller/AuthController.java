package com.example.msuser.controller;

import com.example.msuser.dto.UserRequest;
import com.example.msuser.repository.UserRepository;
import com.example.msuser.entity.User;
import com.example.msuser.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody UserRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {
            // 實務需加密驗證密碼！
            String token = jwtUtil.generateToken(userOpt.get().getUsername(), userOpt.get().getRole());
            return token;
        }
        throw new RuntimeException("Invalid credentials");
    }
}
