package com.dke.foerderportal.antragstellung.controller;


import com.dke.foerderportal.shared.model.Foerderantrag;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.service.FoerderantragService;
import com.dke.foerderportal.shared.service.FormularService;
import com.dke.foerderportal.shared.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foerderantraege")
@RequiredArgsConstructor
public class FoerderantragController {
    private final FoerderantragService foerderantragService;
    private final UserService userService;

    @GetMapping("/my")
    public ResponseEntity<List<Foerderantrag>> getMyAntraege(@AuthenticationPrincipal Jwt jwt) {

        String auth0Id = jwt.getSubject();
        User user = userService.getUserByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("Testuser nicht gefunden"));
        return ResponseEntity.ok(foerderantragService.getAntraegeByUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Foerderantrag> getAntragById(@PathVariable Long id) {
        Foerderantrag antrag = foerderantragService.getAntragById(id);
        return ResponseEntity.ok(antrag);
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

}
