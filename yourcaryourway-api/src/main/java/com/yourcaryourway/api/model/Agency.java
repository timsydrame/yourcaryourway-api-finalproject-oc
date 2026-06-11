package com.yourcaryourway.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "agencies", indexes = {
        @Index(name = "idx_agency_city_country", columnList = "city,country")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$", message = "Le pays doit être un code ISO sur 2 lettres majuscules, ex: FR, GB, US")
    @Column(nullable = false, length = 2)
    private String country;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String address;

    @Size(max = 20)
    @Column(length = 20)
    private String phone;
}