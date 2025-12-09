package com.dke.foerderportal.antragstellung.controller;

import com.dke.foerderportal.shared.model.Nachricht;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.service.NachrichtService;
import com.dke.foerderportal.shared.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/foerderantraege/{antragId}/nachrichten")
@RequiredArgsConstructor
public class NachrichtController {

    private final NachrichtService nachrichtService;
    private final UserService userService;

    /**
     * Alle Nachrichten zu einem Antrag abrufen
     * GET /api/foerderantraege/1/nachrichten
     */
    @GetMapping
    public ResponseEntity<List<Nachricht>> getNachrichten(@PathVariable Long antragId) {
        List<Nachricht> nachrichten = nachrichtService.getNachrichtenByAntragId(antragId);
        return ResponseEntity.ok(nachrichten);
    }

    /**
     * Neue Nachricht erstellen
     * POST /api/foerderantraege/1/nachrichten
     * Body: { "inhalt": "Meine Nachricht" }
     */
    @PostMapping
    public ResponseEntity<Nachricht> createNachricht(
            @PathVariable Long antragId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String auth0Id = jwt.getSubject();
        User sender = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden"));

        String inhalt = body.get("inhalt");
        if (inhalt == null || inhalt.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Nachricht nachricht = nachrichtService.createNachricht(antragId, inhalt, sender);
        return ResponseEntity.ok(nachricht);
    }

    /**
     * Nachricht löschen
     * DELETE /api/foerderantraege/1/nachrichten/5
     */
    @DeleteMapping("/{nachrichtId}")
    public ResponseEntity<Void> deleteNachricht(@PathVariable Long nachrichtId) {
        nachrichtService.deleteNachricht(nachrichtId);
        return ResponseEntity.noContent().build();
    }
}