package com.Foerderportal.Backend.controller;

import com.Foerderportal.Backend.model.Formular;
import com.Foerderportal.Backend.model.User;
import com.Foerderportal.Backend.service.FormularService;
import com.Foerderportal.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/formulare")
@RequiredArgsConstructor
public class FormularController {

    private final FormularService formularService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Formular>> getAllFormulare() {
        return ResponseEntity.ok(formularService.getAllFormulare());
    }

    @GetMapping("/veroeffentlicht")
    public ResponseEntity<List<Formular>> getVeroeffentlichteFormulare() {
        return ResponseEntity.ok(formularService.getVeroeffentlichteFormulare());
    }

    @GetMapping("/kategorie/{kategorie}")
    public ResponseEntity<List<Formular>> getFormulareByKategorie(@PathVariable String kategorie) {
        return ResponseEntity.ok(formularService.getFormulareByKategorie(kategorie));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formular> getFormularById(@PathVariable Long id) {
        return ResponseEntity.ok(formularService.getFormularById(id));
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