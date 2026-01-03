package com.dke.foerderportal.formularsammlung.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
@Data
public class AntragRequest {

    private Long formularId;
    private String titel;
    private String beschreibung;
    private Map<String, Object> antworten;
    private BigDecimal betrag;
}
