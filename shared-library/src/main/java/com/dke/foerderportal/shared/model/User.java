package com.dke.foerderportal.shared.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;


@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "auth0_id", unique = true)
    private String auth0Id;

    private String firma;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRolle rolle = UserRolle.ANTRAGSTELLER;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}