package com.dke.foerderportal.antragsverwaltung.controller;

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
@RequestMapping({
        "/api/antraege-verwaltung/{antragId}/nachrichten",
        "/api/foerderantraege/{antragId}/nachrichten"
})
@RequiredArgsConstructor
public class NachrichtenVerwaltungController {

    private final NachrichtService nachrichtService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Nachricht>> getNachrichten(@PathVariable Long antragId) {
        return ResponseEntity.ok(nachrichtService.getNachrichtenByAntragId(antragId));
    }

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

        return ResponseEntity.ok(nachrichtService.createNachricht(antragId, inhalt, sender));
    }
}
