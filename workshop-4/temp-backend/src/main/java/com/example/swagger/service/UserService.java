package com.example.swagger.service;

import com.example.swagger.model.User;
import com.example.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        // Set default values if not provided
        if (user.getMembershipLevel() == null) {
            user.setMembershipLevel(User.MembershipLevel.BRONZE);
        }
        if (user.getPoints() == null) {
            user.setPoints(0);
        }
        if (user.getMemberSince() == null) {
            user.setMemberSince(LocalDateTime.now());
        }
        
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Check if email is being changed and if new email already exists
        if (!user.getEmail().equals(userDetails.getEmail()) && 
            userRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("Email already exists: " + userDetails.getEmail());
        }

        // Update fields
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhone(userDetails.getPhone());
        user.setEmail(userDetails.getEmail());
        
        if (userDetails.getMembershipLevel() != null) {
            user.setMembershipLevel(userDetails.getMembershipLevel());
        }
        if (userDetails.getPoints() != null) {
            user.setPoints(userDetails.getPoints());
        }
        if (userDetails.getMemberSince() != null) {
            user.setMemberSince(userDetails.getMemberSince());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    // Additional methods for points management
    public User addPoints(Long userId, Integer pointsToAdd) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.addPoints(pointsToAdd);
        return userRepository.save(user);
    }

    public User deductPoints(Long userId, Integer pointsToDeduct) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        if (!user.deductPoints(pointsToDeduct)) {
            throw new RuntimeException("Insufficient points. Current balance: " + user.getPoints());
        }
        
        return userRepository.save(user);
    }

    // Method to upgrade membership level
    public User upgradeMembership(Long userId, User.MembershipLevel newLevel) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.setMembershipLevel(newLevel);
        return userRepository.save(user);
    }
}