package com.dke.foerderportal.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "foerderantraege")
@Data
@NoArgsConstructor
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

    @JsonIgnore
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

    @Column(name = "formular_id")
    private Long formularId;

    @Column(name = "formular_version")
    private Integer formularVersion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "formular_snapshot", columnDefinition = "jsonb")
    private JsonNode formularSnapshot;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "formular_antworten", columnDefinition = "jsonb")
    private JsonNode formularAntworten;

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