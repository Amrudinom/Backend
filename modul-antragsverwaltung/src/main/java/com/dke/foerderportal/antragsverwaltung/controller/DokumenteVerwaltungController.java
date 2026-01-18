package com.dke.foerderportal.antragsverwaltung.controller;

import com.dke.foerderportal.shared.model.Dokument;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.service.DokumentService;
import com.dke.foerderportal.shared.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/antraege-verwaltung/{antragId}/dokumente")
@RequiredArgsConstructor
public class DokumenteVerwaltungController {

    private final DokumentService dokumentService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Dokument>> getDokumente(@PathVariable Long antragId) {
        return ResponseEntity.ok(dokumentService.getDokumenteByAntragId(antragId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Dokument> uploadDokument(
            @PathVariable Long antragId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt
    ) {
        try {
            String auth0Id = jwt.getSubject();

            User uploader = userService.getUserByAuth0Id(auth0Id)
                    .orElseThrow(() -> new RuntimeException("User nicht gefunden"));

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(dokumentService.uploadDokument(antragId, file, uploader));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{dokumentId}/download")
    public ResponseEntity<Resource> downloadDokument(@PathVariable Long dokumentId) {
        try {
            Dokument dokument = dokumentService.getDokumentById(dokumentId);

            Path filePath = Paths.get(dokument.getFilepath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(dokument.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + dokument.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
