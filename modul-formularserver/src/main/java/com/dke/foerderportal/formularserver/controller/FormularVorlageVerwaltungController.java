package com.dke.foerderportal.formularserver.controller;

import com.dke.foerderportal.shared.model.Formularvorlage;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.service.FormularvorlageService;
import com.dke.foerderportal.shared.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formularvorlagen-verwaltung")
@RequiredArgsConstructor
public class FormularVorlageVerwaltungController {
    private final FormularvorlageService formularvorlageService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Formularvorlage>> getAllVorlagen() {
        return ResponseEntity.ok(formularvorlageService.getAllVorlagen());
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
