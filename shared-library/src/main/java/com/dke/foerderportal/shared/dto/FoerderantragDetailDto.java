package com.dke.foerderportal.shared.dto;

import com.dke.foerderportal.shared.model.AntragStatus;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FoerderantragDetailDto(
        Long id,
        String titel,
        String beschreibung,
        BigDecimal betrag,
        AntragStatus status,
        LocalDateTime eingereichtAm,
        LocalDateTime bearbeitetAm,
        String ablehnungsgrund,
        Long formularId,
        Integer formularVersion,
        Object formularSnapshot,
        Object formularAntworten,
        Long antragstellerId,
        String antragstellerName,
        Long bearbeiterId,
        String bearbeiterName
) {}

