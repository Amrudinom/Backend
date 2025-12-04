package com.dke.foerderportal.formularsammlung.controller;

import com.dke.foerderportal.shared.model.Formular;
import com.dke.foerderportal.shared.service.FormularService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/formulare")
@RequiredArgsConstructor
public class FormularController {
    private final FormularService formularService;

    @GetMapping  // Alle veröffentlichten Formulare anzeigen
    public List<Formular> alleFormulare() {
        return formularService.getVeroeffentlichteFormulare();
    }

    @GetMapping("/{id}")  // Ein Formular zum Ausfüllen öffnen
    public Formular formularDetail(@PathVariable Long id) {
        return formularService.getFormularById(id);
    }

    @GetMapping("/kategorie/{kategorie}")
    public ResponseEntity<List<Formular>> getFormulareByKategorie(@PathVariable String kategorie) {
        return ResponseEntity.ok(formularService.getFormulareByKategorie(kategorie));
    }

}
