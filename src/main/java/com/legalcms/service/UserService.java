package com.legalcms.service;

import com.legalcms.dto.UserResponse;
import com.legalcms.model.User;
import com.legalcms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getUserProfile(String email) {
        log.info("Fetching user profile for email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUserProfile(String email, com.legalcms.dto.UpdateUserRequest request) {
        log.info("Updating user profile for email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null)
            user.setFullName(request.getFullName());
        if (request.getPhone() != null)
            user.setPhone(request.getPhone());
        if (request.getBarNumber() != null)
            user.setBarNumber(request.getBarNumber());
        if (request.getFirmName() != null)
            user.setFirmName(request.getFirmName());
        if (request.getSpecialization() != null)
            user.setSpecialization(request.getSpecialization());
        if (request.getExperience() != null)
            user.setExperience(request.getExperience());

        user = userRepository.save(user);
        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .phone(user.getPhone())
                .barNumber(user.getBarNumber())
                .firmName(user.getFirmName())
                .specialization(user.getSpecialization())
                .experience(user.getExperience())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
