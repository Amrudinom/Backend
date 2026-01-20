package com.dke.foerderportal.shared.service;

import com.dke.foerderportal.shared.dto.AntragFormularViewDto;
import com.dke.foerderportal.shared.model.Foerderantrag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AntragFormularViewService {

    private final FoerderantragService foerderantragService;

    public AntragFormularViewDto getFormularView(Long antragId) {
        Foerderantrag antrag = foerderantragService.getAntragById(antragId);
        if (antrag == null) {
            throw new EntityNotFoundException("Antrag nicht gefunden");
        }

        return new AntragFormularViewDto(
                antrag.getId(),
                antrag.getStatus(),
                antrag.getEingereichtAm(),
                antrag.getAntragsteller().getName(),
                antrag.getAblehnungsgrund(),
                antrag.getFormularSnapshot(),    // neu
                antrag.getFormularAntworten()    // neu
        );
    }
}
