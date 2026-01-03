package com.dke.foerderportal.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "formularfelder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formularfeld {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formular_id", nullable = false)
    private Formular formular;

    // Feldname (technischer Name)
    @Column(nullable = false)
    @JsonProperty("feldName")  // Frontend sendet "feldName"
    private String feldName;

    // Anzeigename (für Benutzer)
    @Column(length = 2000)
    @JsonProperty("label")  // Frontend sendet "label"
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("feldTyp")  // Frontend sendet "feldTyp"
    private FeldTyp feldTyp;

    @Column(name = "pflichtfeld")
    @JsonProperty("pflichtfeld")  // Frontend sendet "pflichtfeld"
    private boolean pflichtfeld;

    // OAuth Auto-Fill
    @Column(name = "oauth_auto_fill", nullable = false)
    @JsonProperty("oauthAutoFill")  // Frontend sendet "oauthAutoFill"
    private Boolean oauthAutoFill = false;

    @Column(name = "oauth_Field_Mapping", nullable = false)
    @JsonProperty("oauthFieldMapping")  // Frontend sendet "oauthFieldMapping"
    private String oauthFieldMapping = "";

    @Column(nullable = false)
    @JsonProperty("anzeigeReihenfolge")  // Frontend sendet "reihenfolge" aber wir müssen es umbenennen
    private Integer anzeigeReihenfolge;

    // Neue Felder für erweiterte Konfiguration
    @Column(length = 500)
    @JsonProperty("placeholder")  // Frontend sendet "placeholder"
    private String placeholder;

    @Column(length = 500)
    @JsonProperty("defaultValue")  // Frontend sendet "defaultValue"
    private String defaultValue;

    // Validierungsfelder
    @Column
    @JsonProperty("minLength")  // Frontend sendet "minLength"
    private Integer minLength;

    @Column
    @JsonProperty("maxLength")  // Frontend sendet "maxLength"
    private Integer maxLength;

    @Column(length = 500)
    @JsonProperty("regexPattern")  // Frontend sendet "regexPattern"
    private String regexPattern;

    // Optionen für SELECT/CHECKBOX_GROUP
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    @JsonProperty("optionen")  // Frontend sendet "optionen"
    private JsonNode optionen;

    // Checkbox Labels
    @Column(length = 100)
    @JsonProperty("checkboxLabelTrue")  // Frontend sendet "checkboxLabelTrue"
    private String checkboxLabelTrue;

    @Column(length = 100)
    @JsonProperty("checkboxLabelFalse")  // Frontend sendet "checkboxLabelFalse"
    private String checkboxLabelFalse;

    // File Upload Konfiguration
    @Column(length = 200)
    @JsonProperty("fileTypes")  // Frontend sendet "fileTypes"
    private String fileTypes;

    @Column
    @JsonProperty("maxFileSize")  // Frontend sendet "maxFileSize"
    private Integer maxFileSize;

    // Numerische Grenzwerte
    @Column
    @JsonProperty("minValue")  // Frontend sendet "minValue"
    private Integer minValue;

    @Column
    @JsonProperty("maxValue")  // Frontend sendet "maxValue"
    private Integer maxValue;

    // Alte Felder (für Kompatibilität)
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode validierungsRegeln;

    @Column(name = "oauth_zuordbar", nullable = false)
    private Boolean oauthZuordbar = false;

    @Column(name = "oauth_vorfeld", nullable = false)
    private Boolean oauthVorfeld = false;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "felddefinition_id")
    private Felddefinition felddefinition;

}