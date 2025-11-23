package com.Foerderportal.Backend.controller;

import com.Foerderportal.Backend.model.AntragStatus;
import com.Foerderportal.Backend.model.Foerderantrag;
import com.Foerderportal.Backend.model.User;
import com.Foerderportal.Backend.service.FoerderantragService;
import com.Foerderportal.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/foerderantraege")
@RequiredArgsConstructor
public class FoerderantragController {

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

    @GetMapping("/my")
    public ResponseEntity<List<Foerderantrag>> getMyAntraege(@AuthenticationPrincipal Jwt jwt) {

        String auth0Id = jwt.getSubject();
        User user = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("Testuser nicht gefunden"));
        return ResponseEntity.ok(foerderantragService.getAntraegeByUser(user));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Foerderantrag>> getAntraegeByStatus(@PathVariable AntragStatus status) {
        return ResponseEntity.ok(foerderantragService.getAntraegeByStatus(status));
    }

    @PostMapping
    public ResponseEntity<Foerderantrag> createAntrag(
            @RequestBody Foerderantrag antrag,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String auth0Id = jwt.getSubject();
        User user = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        antrag.setAntragsteller(user);
        Foerderantrag created = foerderantragService.createAntrag(antrag, user.getId());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Foerderantrag> updateAntrag(
            @PathVariable Long id,
            @RequestBody Foerderantrag antrag
    ) {
        Foerderantrag updated = foerderantragService.updateAntrag(id, antrag);
        return ResponseEntity.ok(updated);
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
}