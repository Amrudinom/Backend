package com.dke.foerderportal.shared.dto;

import lombok.Data;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

@Data
public class CreateAntragRequest {

    @NotNull
    private String titel;
    private String beschreibung;
    @NotNull
    private BigDecimal betrag;
    @NotNull
    private Long antragstellerId;
}