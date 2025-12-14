package com.dke.foerderportal.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FormularStatus {
    DRAFT("DRAFT", "ENTWURF"),
    PUBLISHED("PUBLISHED", "VERÖFFENTLICHT"),
    ARCHIVED("ARCHIVED", "ARCHIVIERT");

    private final String backendValue;
    private final String displayName;


    FormularStatus(String backendValue, String displayName) {
        this.backendValue = backendValue;
        this.displayName = displayName;
    }

    @JsonValue
    public String getBackendValue() {
        return backendValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static FormularStatus fromString(String value) {
        if (value == null) return DRAFT;

        for (FormularStatus status : FormularStatus.values()) {
            if (status.backendValue.equalsIgnoreCase(value) ||
                    status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }

        return DRAFT;
    }
}