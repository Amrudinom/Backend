package com.dke.foerderportal.antragsverwaltung.dto;

import com.dke.foerderportal.shared.model.AntragStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FoerderantragListDto(
        Long id,
        String titel,
        String beschreibung,
        BigDecimal betrag,
        AntragStatus status,
        LocalDateTime eingereichtAm,
        String ablehnungsgrund
) {}
