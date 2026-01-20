package com.dke.foerderportal.shared.dto;

import com.dke.foerderportal.shared.model.AntragStatus;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public record AntragFormularViewDto(
        Long antragId,
        AntragStatus status,
        LocalDateTime eingereichtAm,
        String antragstellerName,
        String ablehnungsgrund,
        JsonNode formularSnapshot,
        JsonNode formularAntworten
) {}

