package com.dke.foerderportal.antragsverwaltung.dto;

import com.dke.foerderportal.shared.model.AntragStatus;

public record UpdateStatusRequest(
        AntragStatus status,
        String grund
) {}
