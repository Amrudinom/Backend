package com.dke.foerderportal.shared.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formularvorlagen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formularvorlage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titel;

    @Column(length = 2000)
    private String beschreibung;

    @Column(nullable = false)
    private String kategorie;

    @Column(nullable = false)
    private Integer version = 1;

    @Column(nullable = false)
    private Boolean istVeroeffentlicht = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ersteller_von")
    private User erstellerVon;

    @Column(name = "erstellt_am", updatable = false)
    private LocalDateTime erstelltAm;

    @Column(name = "aktualisiert_am")
    private LocalDateTime aktualisiertAm;

    @OneToMany(mappedBy = "formularvorlage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Felddefinition> felddefinitionen = new ArrayList<>();

    @OneToMany(mappedBy = "formularvorlage", cascade = CascadeType.ALL)
    private List<Formular> formulare = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        erstelltAm = LocalDateTime.now();
        aktualisiertAm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        aktualisiertAm = LocalDateTime.now();
    }
}