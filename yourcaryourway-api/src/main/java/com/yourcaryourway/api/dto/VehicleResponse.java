package com.yourcaryourway.api.dto;

import lombok.*;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleResponse {
    private UUID id;
    private String brand;
    private String model;
    private Integer year;
    private String acrissCode;
    private String color;
    private String fuelType;
    private String transmission;
    private Integer seats;
    private String photoUrl;
}