package com.dke.foerderportal.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "nachrichten")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nachricht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 5000)
    private String inhalt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foerderantrag_id", nullable = false)
    private Foerderantrag foerderantrag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gesendet_von", nullable = false)
    private User gesendetVon;

    @Column(name = "gesendet_am", updatable = false)
    private LocalDateTime gesendetAm;

    @PrePersist
    protected void onCreate() {
        gesendetAm = LocalDateTime.now();
    }
}