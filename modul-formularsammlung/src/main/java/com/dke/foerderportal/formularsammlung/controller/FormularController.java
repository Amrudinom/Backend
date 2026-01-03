package com.dke.foerderportal.formularsammlung.controller;

import com.dke.foerderportal.formularsammlung.dto.AntragRequest;
import com.dke.foerderportal.shared.model.Foerderantrag;
import com.dke.foerderportal.shared.model.Formular;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.service.FoerderantragService;
import com.dke.foerderportal.shared.service.FormularService;
import com.dke.foerderportal.shared.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/formulare")
@RequiredArgsConstructor
public class FormularController {
    private final FormularService formularService;
    private final FoerderantragService foerderantragService;
    private final UserService userService;

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

    @PostMapping
    public ResponseEntity<Foerderantrag> createAntrag(
            @RequestBody AntragRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String auth0Id = jwt.getSubject();
        User user = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Formular formular = formularService.getFormularById(request.getFormularId());

        Foerderantrag antrag = new Foerderantrag();
        antrag.setTitel(request.getTitel());
        antrag.setBeschreibung(request.getBeschreibung());
        antrag.setBetrag(request.getBetrag());

        Foerderantrag created = foerderantragService.createAntrag(antrag, user.getId());

        return ResponseEntity.ok(created);
    }

}
