package com.yourcaryourway.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicles", indexes = {
        @Index(name = "idx_vehicle_acriss",    columnList = "acriss_code"),
        @Index(name = "idx_vehicle_available", columnList = "is_available")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String brand;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String model;

    @NotNull
    @Min(1900) @Max(2100)
    @Column(nullable = false)
    private Integer year;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{4}$", message = "Le code ACRISS doit contenir 4 lettres majuscules, ex: ECMR")
    @Column(name = "acriss_code", nullable = false, length = 4)
    private String acrissCode;

    @Size(max = 30)
    @Column(length = 30)
    private String color;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 20)
    private FuelType fuelType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Transmission transmission;

    @NotNull
    @Min(1) @Max(9)
    @Column(nullable = false)
    private Integer seats;

    @Size(max = 500)
    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Builder.Default
    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum FuelType { ESSENCE, DIESEL, ELECTRIQUE, HYBRIDE }
    public enum Transmission { MANUELLE, AUTOMATIQUE }
}