package com.dke.foerderportal.shared.model;

import com.fasterxml.jackson.databind.JsonNode;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "felddefinitionen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Felddefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vorlage_id")
    private Formularvorlage formularvorlage;

    @Column(nullable = false)
    private String feldName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeldTyp feldTyp;

    @Column(length = 2000)
    private String beschriftung;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode validierungsRegeln;

    @Column(nullable = false)
    private Boolean oauthZuordbar = false;

    @Column(nullable = false)
    private Boolean oauthVorfeld = false;

    @Column(nullable = false)
    private Integer anzeigeReihenfolge;

    @OneToMany(mappedBy = "felddefinition", cascade = CascadeType.ALL)
    private List<Formularfeld> formularfelder = new ArrayList<>();
}