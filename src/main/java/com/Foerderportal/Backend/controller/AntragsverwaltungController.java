package com.Foerderportal.Backend.controller;

import com.Foerderportal.Backend.model.Foerderantrag;
import com.Foerderportal.Backend.service.FoerderantragService;
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
    public Foerderantrag createAntrag(@RequestBody Foerderantrag antrag) {
        return foerderantragService.createAntrag(antrag);

    }

    @GetMapping("/{id}")
    public Foerderantrag getById(@PathVariable Long id) {
        return foerderantragService.getAntragById(id);
    }
}
