package com.dke.foerderportal.formularserver.controller;


import com.dke.foerderportal.shared.model.Formular;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.service.FormularService;
import com.dke.foerderportal.shared.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formulare-verwaltung")
@RequiredArgsConstructor
public class FormularVerwaltungController {
    private final FormularService formularService;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<Formular>> getAllFormulare() {
        return ResponseEntity.ok(formularService.getAllFormulare());
    }

    @PostMapping
    public ResponseEntity<Formular> createFormular(
            @RequestBody Formular formular,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String auth0Id = jwt.getSubject();
        User ersteller = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden"));

        Formular created = formularService.createFormular(formular, ersteller);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formular> updateFormular(
            @PathVariable Long id,
            @RequestBody Formular formular
    ) {
        Formular updated = formularService.updateFormular(id, formular);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/veroeffentlichen")
    public ResponseEntity<Formular> veroeffentlichenFormular(@PathVariable Long id) {
        Formular formular = formularService.veroeffentlichenFormular(id);
        return ResponseEntity.ok(formular);
    }

    @PostMapping("/{id}/zurueckziehen")
    public ResponseEntity<Formular> zurueckziehenFormular(@PathVariable Long id) {
        Formular formular = formularService.zurueckziehenFormular(id);
        return ResponseEntity.ok(formular);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormular(@PathVariable Long id) {
        formularService.deleteFormular(id);
        return ResponseEntity.noContent().build();
    }
}
