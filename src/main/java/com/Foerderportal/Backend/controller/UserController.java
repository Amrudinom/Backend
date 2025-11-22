package com.Foerderportal.Backend.controller;

import com.Foerderportal.Backend.model.User;
import com.Foerderportal.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String auth0Id = jwt.getSubject();

        // Versuche Email und Name aus verschiedenen Claims zu holen
        String email = jwt.getClaimAsString("email");
        if (email == null) {
            email = jwt.getClaimAsString("https://foerderportal-api/email");
        }
        if (email == null) {
            email = jwt.getClaimAsString("sub"); // Fallback: Nutze sub als Email
        }

        String name = jwt.getClaimAsString("name");
        if (name == null) {
            name = jwt.getClaimAsString("https://foerderportal-api/name");
        }
        if (name == null) {
            name = jwt.getClaimAsString("nickname");
        }
        if (name == null) {
            name = "User"; // Fallback
        }

        System.out.println("📌 JWT Subject: " + auth0Id);
        System.out.println("📌 Email: " + email);
        System.out.println("📌 Name: " + name);
        System.out.println("📌 All Claims: " + jwt.getClaims());

        User user = userService.getOrCreateUserFromAuth0(auth0Id, email, name);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}