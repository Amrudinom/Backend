package com.dke.foerderportal.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formulare")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titel;

    @Column(length = 2000)
    private String beschreibung;

    @Column(nullable = false)
    private String kategorie;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode schema;

    @Column(nullable = false)
    private Boolean istVeroeffentlicht = false;

    @Column(name = "erstellt_am", updatable = false)
    private LocalDateTime erstelltAm;

    @Column(name = "aktualisiert_am")
    private LocalDateTime aktualisiertAm;

    @Column(nullable = false)
    private Integer version = 1;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ersteller_von")
    private User erstellerVon;

    @OneToMany(mappedBy = "formular", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("anzeigeReihenfolge ASC")
    private List<Formularfeld> felder = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vorlage_id")
    private Formularvorlage formularvorlage;

    @PrePersist
    protected void onCreate() {
        erstelltAm = LocalDateTime.now();
        aktualisiertAm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        aktualisiertAm = LocalDateTime.now();
    }

    public void addFeld(Formularfeld feld) {
        felder.add(feld);
        feld.setFormular(this);
    }

    public void removeFeld(Formularfeld feld) {
        felder.remove(feld);
        feld.setFormular(null);
    }
}