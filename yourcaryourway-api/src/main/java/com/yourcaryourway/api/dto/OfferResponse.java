package com.yourcaryourway.api.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OfferResponse {
    private UUID id;
    private VehicleResponse vehicle;
    private AgencyResponse agencyDeparture;
    private AgencyResponse agencyReturn;
    private BigDecimal pricePerDay;
    private boolean available;
}