package com.dke.foerderportal.antragsverwaltung.controller;


<<<<<<< Updated upstream
=======
import com.dke.foerderportal.antragsverwaltung.dto.FoerderantragListDto;
import com.dke.foerderportal.antragsverwaltung.dto.UpdateStatusRequest;
import com.dke.foerderportal.shared.dto.AntragFormularViewDto;
>>>>>>> Stashed changes
import com.dke.foerderportal.shared.dto.CreateAntragRequest;
import com.dke.foerderportal.shared.model.AntragStatus;
import com.dke.foerderportal.shared.model.Foerderantrag;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.service.FoerderantragService;
import com.dke.foerderportal.shared.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@RequestMapping("/api/antraege-verwaltung")
@RequiredArgsConstructor
public class AntragBearbeitungController {
    private final FoerderantragService foerderantragService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Foerderantrag>> getAllAntraege() {
        return ResponseEntity.ok(foerderantragService.getAllAntraege());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Foerderantrag> getAntragById(@PathVariable Long id) {
        Foerderantrag antrag = foerderantragService.getAntragById(id);
        return ResponseEntity.ok(antrag);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Foerderantrag>> getAntraegeByStatus(@PathVariable AntragStatus status) {
        return ResponseEntity.ok(foerderantragService.getAntraegeByStatus(status));
    }

    @PostMapping("/{id}/genehmigen")
    public ResponseEntity<Foerderantrag> genehmigenAntrag(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String auth0Id = jwt.getSubject();
        User bearbeiter = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Foerderantrag antrag = foerderantragService.genehmigenAntrag(id, bearbeiter);
        return ResponseEntity.ok(antrag);
    }

    @PostMapping("/{id}/ablehnen")
    public ResponseEntity<Foerderantrag> ablehnenAntrag(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String auth0Id = jwt.getSubject();
        User bearbeiter = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String grund = body.get("grund");
        Foerderantrag antrag = foerderantragService.ablehnenAntrag(id, bearbeiter, grund);
        return ResponseEntity.ok(antrag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAntrag(@PathVariable Long id) {
        foerderantragService.deleteAntrag(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CreateAntragRequest>> filter(
            @RequestParam(required = false) AntragStatus status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) LocalDate to
    ) {
        List<CreateAntragRequest> result = foerderantragService.filterAntraege(status, userId, from, to);
        return ResponseEntity.ok(result);
    }
<<<<<<< Updated upstream
=======

    @GetMapping("/{id}/formular")
    public ResponseEntity<AntragFormularViewDto> getFormular(@PathVariable Long id) {
        return ResponseEntity.ok(antragFormularViewService.getFormularView(id));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Foerderantrag> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest body,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String auth0Id = jwt.getSubject();
        User bearbeiter = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Foerderantrag antrag = foerderantragService.updateStatus(id, body.status(), bearbeiter, body.grund());
        return ResponseEntity.ok(antrag);
    }

>>>>>>> Stashed changes

}
