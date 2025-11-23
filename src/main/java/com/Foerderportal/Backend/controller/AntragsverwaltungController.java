package com.Foerderportal.Backend.controller;

import com.Foerderportal.Backend.dto.CreateAntragRequest;
import com.Foerderportal.Backend.model.Foerderantrag;
import com.Foerderportal.Backend.service.FoerderantragService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antraege")
public class AntragsverwaltungController {

    private final FoerderantragService foerderantragService;

    public AntragsverwaltungController(FoerderantragService foerderantragService) {
        this.foerderantragService = foerderantragService;
    }

    @GetMapping
    public List<Foerderantrag> getAll() {
        return foerderantragService.getAllAntraege();
    }

    @PostMapping
    public ResponseEntity<String> createAntrag(@RequestBody CreateAntragRequest request) {
        Foerderantrag antrag = new Foerderantrag();
        antrag.setTitel(request.getTitel());
        antrag.setBeschreibung(request.getBeschreibung());
        antrag.setBetrag(request.getBetrag());

        foerderantragService.createAntrag(antrag, request.getAntragstellerId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Antrag erfolgreich eingereicht.");
    }

    @GetMapping("/{id}")
    public Foerderantrag getById(@PathVariable Long id) {
        return foerderantragService.getAntragById(id);
    }
}
