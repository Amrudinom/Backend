package com.Foerderportal.Backend.service;

import com.Foerderportal.Backend.model.User;
import com.Foerderportal.Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByAuth0Id(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id);
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email already exists: " + user.getEmail());
        }

        User savedUser = userRepository.save(user);
        log.info("User created: {}", savedUser.getEmail());

        // Send welcome email (don't fail if email fails)
        try {
            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());
        } catch (Exception e) {
            log.error("Failed to send welcome email", e);
        }

        return savedUser;
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        user.setName(userDetails.getName());
        user.setFirma(userDetails.getFirma());
        user.setRolle(userDetails.getRolle());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("User deleted: {}", id);
    }

    /**
     * Get existing user by Auth0 ID or create new user
     * Used for Auth0 login
     */
    public User getOrCreateUserFromAuth0(String auth0Id, String email, String name) {
        return userRepository.findByAuth0Id(auth0Id)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setAuth0Id(auth0Id);
                    newUser.setEmail(email);
                    newUser.setName(name);
                    return createUser(newUser);
                });
    }
}