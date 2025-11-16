package com.Foerderportal.Backend.controller;

import com.Foerderportal.Backend.model.Formularvorlage;
import com.Foerderportal.Backend.model.User;
import com.Foerderportal.Backend.service.FormularvorlageService;
import com.Foerderportal.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/formularvorlagen")
@RequiredArgsConstructor
public class FormularvorlageController {

    private final FormularvorlageService formularvorlageService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Formularvorlage>> getAllVorlagen() {
        return ResponseEntity.ok(formularvorlageService.getAllVorlagen());
    }

    @GetMapping("/veroeffentlicht")
    public ResponseEntity<List<Formularvorlage>> getVeroeffentlichteVorlagen() {
        return ResponseEntity.ok(formularvorlageService.getVeroeffentlichteVorlagen());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formularvorlage> getVorlageById(@PathVariable Long id) {
        return ResponseEntity.ok(formularvorlageService.getVorlageById(id));
    }

    @PostMapping
    public ResponseEntity<Formularvorlage> createVorlage(
            @RequestBody Formularvorlage vorlage,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String auth0Id = jwt.getSubject();
        User ersteller = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden"));

        Formularvorlage created = formularvorlageService.createVorlage(vorlage, ersteller);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formularvorlage> updateVorlage(
            @PathVariable Long id,
            @RequestBody Formularvorlage vorlage
    ) {
        Formularvorlage updated = formularvorlageService.updateVorlage(id, vorlage);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVorlage(@PathVariable Long id) {
        formularvorlageService.deleteVorlage(id);
        return ResponseEntity.noContent().build();
    }
}