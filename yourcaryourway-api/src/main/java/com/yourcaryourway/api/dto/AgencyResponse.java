package com.yourcaryourway.api.dto;

import lombok.*;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AgencyResponse {
    private UUID id;
    private String name;
    private String city;
    private String country;
    private String address;
    private String phone;
}