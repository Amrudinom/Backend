package com.dke.foerderportal.shared.dto;

public record DokumentDto(
        Long id,
        String filename,
        String contentType,
        long size,
        String uploadedAt,
        Long uploaderId
) {}
