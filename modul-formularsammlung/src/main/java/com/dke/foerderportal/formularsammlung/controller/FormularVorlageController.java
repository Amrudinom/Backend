package com.dke.foerderportal.formularsammlung.controller;

import com.dke.foerderportal.shared.model.Formularvorlage;
import com.dke.foerderportal.shared.service.FormularvorlageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/formularvorlagen")
@RequiredArgsConstructor
public class FormularVorlageController {
    private final FormularvorlageService formularvorlageService;

    @GetMapping("/veroeffentlicht")
    public ResponseEntity<List<Formularvorlage>> getVeroeffentlichteVorlagen() {
        return ResponseEntity.ok(formularvorlageService.getVeroeffentlichteVorlagen());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formularvorlage> getVorlageById(@PathVariable Long id) {
        return ResponseEntity.ok(formularvorlageService.getVorlageById(id));
    }
}
