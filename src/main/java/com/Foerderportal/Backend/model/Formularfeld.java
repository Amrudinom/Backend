package com.Foerderportal.Backend.model;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formular_id", nullable = false)
    private Formular formular;

    @Column(nullable = false)
    private String feldName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeldTyp feldTyp;

    @Column(length = 2000)
    private String beschriftung;

    @Column(nullable = false)
    private Boolean oauthZuordbar = false;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode validierungsRegeln;

    @Column(nullable = false)
    private Boolean oauthVorfeld = false;

    @Column(nullable = false)
    private Integer anzeigeReihenfolge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "felddefinition_id")
    private Felddefinition felddefinition;
}