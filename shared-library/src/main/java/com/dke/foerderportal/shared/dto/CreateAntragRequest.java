package com.dke.foerderportal.shared.dto;

import com.dke.foerderportal.shared.model.AntragStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CreateAntragRequest {

    private Long id;
    @NotNull
    private String titel;
    private String beschreibung;
    @NotNull
    private BigDecimal betrag;
    private AntragStatus status;
    private LocalDateTime eingereichtAm;
    private String antragstellerName;
}