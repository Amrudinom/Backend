package com.dke.foerderportal.antragsverwaltung.controller;

import com.dke.foerderportal.shared.dto.DokumentDto;
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
@RequestMapping({"/api/antraege-verwaltung/{antragId}/dokumente", "/api/foerderantraege/{antragId}/dokumente"})
@RequiredArgsConstructor
public class DokumenteVerwaltungController {

    private final DokumentService dokumentService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<DokumentDto>> getDokumente(@PathVariable Long antragId) {
        return ResponseEntity.ok(dokumentService.getDokumenteByAntragId(antragId).stream().map(d -> new DokumentDto(d.getId(), d.getFilename(), d.getContentType(), d.getFileSize(), d.getUploadedAt().toString(), d.getUploadedBy().getId())).toList());
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DokumentDto> uploadDokument(@PathVariable Long antragId, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal Jwt jwt) throws IOException {

        String auth0Id = jwt.getSubject();
        User uploader = userService.getUserByAuth0Id(auth0Id).orElseThrow(() -> new RuntimeException("User nicht gefunden"));

        Dokument dokument = dokumentService.uploadDokument(antragId, file, uploader);

        var dto = new DokumentDto(dokument.getId(), dokument.getFilename(), dokument.getContentType(), dokument.getFileSize(), dokument.getUploadedAt().toString(), uploader.getId());

        return ResponseEntity.ok(dto);
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

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(dokument.getContentType())).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dokument.getFilename() + "\"").body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
