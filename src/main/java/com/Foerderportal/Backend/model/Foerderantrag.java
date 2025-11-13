package com.Foerderportal.Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "foerderantraege")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Foerderantrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titel;

    @Column(length = 5000)
    private String beschreibung;

    @Column(nullable = false)
    private BigDecimal betrag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User antragsteller;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AntragStatus status = AntragStatus.EINGEREICHT;

    @Column(name = "eingereicht_am", updatable = false)
    private LocalDateTime eingereichtAm;

    @Column(name = "bearbeitet_am")
    private LocalDateTime bearbeitetAm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bearbeiter_id")
    private User bearbeiter;

    @Column(length = 2000)
    private String ablehnungsgrund;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (eingereichtAm == null) {
            eingereichtAm = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}