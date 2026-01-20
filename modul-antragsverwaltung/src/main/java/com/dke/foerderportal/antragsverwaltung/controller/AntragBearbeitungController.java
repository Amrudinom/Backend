package com.dke.foerderportal.antragsverwaltung.controller;

import com.dke.foerderportal.antragsverwaltung.dto.UpdateStatusRequest;
import com.dke.foerderportal.shared.dto.AntragFormularViewDto;
import com.dke.foerderportal.shared.dto.CreateAntragRequest;
import com.dke.foerderportal.shared.dto.FoerderantragDetailDto;
import com.dke.foerderportal.shared.dto.FoerderantragListDto;
import com.dke.foerderportal.shared.model.AntragStatus;
import com.dke.foerderportal.shared.model.Foerderantrag;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.service.AntragFormularViewService;
import com.dke.foerderportal.shared.service.FoerderantragService;
import com.dke.foerderportal.shared.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@RequestMapping("/api/antraege-verwaltung")
@RequiredArgsConstructor
public class AntragBearbeitungController {

    private final FoerderantragService foerderantragService;
    private final UserService userService;
    private final AntragFormularViewService antragFormularViewService;

    // =========================================================
    // LISTE (DTO statt Entity -> verhindert 500 durch Lazy-Loading)
    // =========================================================
    @GetMapping
    public ResponseEntity<List<FoerderantragListDto>> getAllAntraege() {
        List<FoerderantragListDto> result = foerderantragService.getAllAntraege().stream()
                .map(a -> new FoerderantragListDto(
                        a.getId(),
                        a.getTitel(),
                        a.getBeschreibung(),
                        a.getBetrag(),
                        a.getStatus(),
                        a.getEingereichtAm()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

    // =========================================================
    // DETAIL (DTO statt Entity -> verhindert Lazy/JSON Probleme)
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<FoerderantragDetailDto> getAntragById(@PathVariable Long id) {
        return ResponseEntity.ok(foerderantragService.getAntragDetailDtoById(id));
    }

    // =========================================================
    // FILTER NACH STATUS (DTO statt Entity)
    // =========================================================
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FoerderantragListDto>> getAntraegeByStatus(@PathVariable AntragStatus status) {
        List<FoerderantragListDto> result = foerderantragService.getAntraegeByStatus(status).stream()
                .map(a -> new FoerderantragListDto(
                        a.getId(),
                        a.getTitel(),
                        a.getBeschreibung(),
                        a.getBetrag(),
                        a.getStatus(),
                        a.getEingereichtAm()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

    // =========================================================
    // GENEHMIGEN / ABLEHNEN (kann bleiben)
    // =========================================================
    @PostMapping("/{id}/genehmigen")
    public ResponseEntity<Foerderantrag> genehmigenAntrag(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        User bearbeiter = getBearbeiter(jwt);
        Foerderantrag antrag = foerderantragService.genehmigenAntrag(id, bearbeiter);
        return ResponseEntity.ok(antrag);
    }

    @PostMapping("/{id}/ablehnen")
    public ResponseEntity<Foerderantrag> ablehnenAntrag(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt
    ) {
        User bearbeiter = getBearbeiter(jwt);
        String grund = body.get("grund");
        Foerderantrag antrag = foerderantragService.ablehnenAntrag(id, bearbeiter, grund);
        return ResponseEntity.ok(antrag);
    }

    // =========================================================
    // STATUS ALLGEMEIN ÄNDERN (User Story)
    // =========================================================
    @PatchMapping("/{id}/status")
    public ResponseEntity<Foerderantrag> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest body,
            @AuthenticationPrincipal Jwt jwt
    ) {
        User bearbeiter = getBearbeiter(jwt);
        Foerderantrag antrag = foerderantragService.updateStatus(id, body.status(), bearbeiter, body.grund());
        return ResponseEntity.ok(antrag);
    }

    // =========================================================
    // FORMULAR VIEW (Snapshot + Antworten + Status + Ablehnungsgrund)
    // =========================================================
    @GetMapping("/{id}/formular")
    public ResponseEntity<AntragFormularViewDto> getFormular(@PathVariable Long id) {
        return ResponseEntity.ok(antragFormularViewService.getFormularView(id));
    }

    // =========================================================
    // LÖSCHEN
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAntrag(@PathVariable Long id) {
        foerderantragService.deleteAntrag(id);
        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // FILTER
    // =========================================================
    @GetMapping("/filter")
    public ResponseEntity<List<CreateAntragRequest>> filter(
            @RequestParam(required = false) AntragStatus status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) LocalDate to
    ) {
        return ResponseEntity.ok(foerderantragService.filterAntraege(status, userId, from, to));
    }

    // =========================================================
    // Helfer: Bearbeiter aus JWT laden
    // =========================================================
    private User getBearbeiter(Jwt jwt) {
        String auth0Id = jwt.getSubject();
        return userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
