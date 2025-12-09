package com.dke.foerderportal.antragstellung.controller;

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
@RequestMapping("/api/foerderantraege/{antragId}/dokumente")
@RequiredArgsConstructor
public class DokumentController {

    private final DokumentService dokumentService;
    private final UserService userService;

    /**
     * Alle Dokumente zu einem Antrag abrufen
     * GET /api/foerderantraege/1/dokumente
     */
    @GetMapping
    public ResponseEntity<List<Dokument>> getDokumente(@PathVariable Long antragId) {
        List<Dokument> dokumente = dokumentService.getDokumenteByAntragId(antragId);
        return ResponseEntity.ok(dokumente);
    }

    /**
     * Dokument hochladen
     * POST /api/foerderantraege/1/dokumente
     * Content-Type: multipart/form-data
     */
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

            Dokument dokument = dokumentService.uploadDokument(antragId, file, uploader);
            return ResponseEntity.ok(dokument);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Dokument herunterladen
     * GET /api/foerderantraege/1/dokumente/5/download
     */
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

    /**
     * Dokument löschen
     * DELETE /api/foerderantraege/1/dokumente/5
     */
    @DeleteMapping("/{dokumentId}")
    public ResponseEntity<Void> deleteDokument(@PathVariable Long dokumentId) {
        try {
            dokumentService.deleteDokument(dokumentId);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}