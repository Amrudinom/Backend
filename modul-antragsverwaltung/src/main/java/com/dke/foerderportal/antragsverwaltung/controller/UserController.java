package com.dke.foerderportal.antragsverwaltung.controller;


import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String auth0Id = jwt.getSubject();


        String email = jwt.getClaimAsString("email");
        if (email == null) {
            email = jwt.getClaimAsString("https://foerderportal-api/email");
        }
        if (email == null) {
            email = jwt.getClaimAsString("sub");
        }

        String name = jwt.getClaimAsString("name");

        if (name == null) {
            name = jwt.getClaimAsString("nickname");
        }
        if (name == null) {
            name = "User";
        }
        User user = userService.getOrCreateUserFromAuth0(auth0Id, email, name);

        return ResponseEntity.ok(user);
    }

}
