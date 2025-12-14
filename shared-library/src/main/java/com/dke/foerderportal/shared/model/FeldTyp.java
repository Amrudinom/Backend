package com.dke.foerderportal.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FeldTyp {
    TEXT("TEXT", "TEXT", "Text"),
    TEXTAREA("TEXTAREA", "TEXTAREA", "Textbereich"),
    EMAIL("EMAIL", "EMAIL", "E-Mail"),
    ZAHL("ZAHL", "NUMBER", "Zahl"),
    DATUM("DATUM", "DATE", "Datum"),
    CHECKBOX("CHECKBOX", "CHECKBOX", "Checkbox"),
    AUSWAHLLISTE("AUSWAHLLISTE", "SELECT", "Dropdown"),
    DATEI_UPLOAD("DATEI_UPLOAD", "FILE_UPLOAD", "Datei-Upload");

    private final String backendValue;
    private final String frontendValue;
    private final String displayName;

    FeldTyp(String backendValue, String frontendValue, String displayName) {
        this.backendValue = backendValue;
        this.frontendValue = frontendValue;
        this.displayName = displayName;
    }

    @JsonValue
    public String getBackendValue() {
        return backendValue;
    }

    public String getFrontendValue() {
        return frontendValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static FeldTyp fromString(String value) {
        if (value == null) {
            return null;
        }

        // Prüfe zuerst die Backend-Werte
        for (FeldTyp typ : FeldTyp.values()) {
            if (typ.backendValue.equalsIgnoreCase(value)) {
                return typ;
            }
        }

        // Prüfe Frontend-Werte (falls Frontend englische Werte sendet)
        for (FeldTyp typ : FeldTyp.values()) {
            if (typ.frontendValue.equalsIgnoreCase(value)) {
                return typ;
            }
        }

        // Fallback für verschiedene Schreibweisen
        switch (value.toUpperCase()) {
            case "DATE":
            case "DATUM":
                return DATUM;
            case "NUMBER":
            case "ZAHL":
                return ZAHL;
            case "TEXT":
            case "TEXTFELD":
                return TEXT;
            case "TEXTAREA":
            case "TEXTBEREICH":
                return TEXTAREA;
            case "EMAIL":
            case "E-MAIL":
                return EMAIL;
            case "CHECKBOX":
                return CHECKBOX;
            case "SELECT":
            case "DROPDOWN":
            case "AUSWAHLLISTE":
                return AUSWAHLLISTE;
            case "FILE_UPLOAD":
            case "DATEI":
            case "DATEI_UPLOAD":
                return DATEI_UPLOAD;
            default:
                throw new IllegalArgumentException("Unbekannter FeldTyp: " + value);
        }
    }
}