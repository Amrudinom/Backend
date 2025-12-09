package com.dke.foerderportal.shared.service;

import com.dke.foerderportal.shared.model.Dokument;
import com.dke.foerderportal.shared.model.Foerderantrag;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.repository.DokumentRepository;
import com.dke.foerderportal.shared.repository.FoerderantragRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DokumentService {

    private final DokumentRepository dokumentRepository;
    private final FoerderantragRepository foerderantragRepository;

    // Ordner wo die Dateien gespeichert werden
    private final String UPLOAD_DIR = "uploads/dokumente/";

    /**
     * Alle Dokumente zu einem Antrag laden
     */
    public List<Dokument> getDokumenteByAntragId(Long antragId) {
        Foerderantrag antrag = foerderantragRepository.findById(antragId)
                .orElseThrow(() -> new RuntimeException("Antrag nicht gefunden"));
        return dokumentRepository.findByFoerderantrag(antrag);
    }

    /**
     * Dokument hochladen
     */
    public Dokument uploadDokument(Long antragId, MultipartFile file, User uploader) throws IOException {
        Foerderantrag antrag = foerderantragRepository.findById(antragId)
                .orElseThrow(() -> new RuntimeException("Antrag nicht gefunden: " + antragId));

        // Ordner erstellen falls nicht existiert
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Eindeutiger Dateiname
        String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(uniqueFilename);

        // Datei speichern
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Dokument in DB speichern
        Dokument dokument = new Dokument();
        dokument.setFilename(file.getOriginalFilename());
        dokument.setFilepath(filePath.toString());
        dokument.setContentType(file.getContentType());
        dokument.setFileSize(file.getSize());
        dokument.setFoerderantrag(antrag);
        dokument.setUploadedBy(uploader);

        Dokument saved = dokumentRepository.save(dokument);
        log.info("Dokument hochgeladen für Antrag {}: {}", antragId, saved.getFilename());

        return saved;
    }

    /**
     * Dokument löschen
     */
    public void deleteDokument(Long id) throws IOException {
        Dokument dokument = dokumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dokument nicht gefunden"));

        // Datei vom Dateisystem löschen
        Path filePath = Paths.get(dokument.getFilepath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Aus DB löschen
        dokumentRepository.deleteById(id);
        log.info("Dokument gelöscht: {}", dokument.getFilename());
    }

    /**
     * Dokument herunterladen
     */
    public Dokument getDokumentById(Long id) {
        return dokumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dokument nicht gefunden"));
    }
}