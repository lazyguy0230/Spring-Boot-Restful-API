package com.example.msuser.service;

import com.example.msuser.dto.UserRequest;
import com.example.msuser.dto.UserResponse;
import com.example.msuser.entity.User;
import com.example.msuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Create
    public UserResponse createUser(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        User saved = userRepository.save(user);
        return toUserResponse(saved);
    }

    // Read all
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    // Read one
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toUserResponse);
    }

    // Update
    public Optional<UserResponse> updateUser(Long id, UserRequest request) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setRole(request.getRole());
            User updated = userRepository.save(user);
            return Optional.of(toUserResponse(updated));
        }
        return Optional.empty();
    }

    // Delete
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 對應 UserEntity 轉 UserResponse
    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        return response;
    }
}
