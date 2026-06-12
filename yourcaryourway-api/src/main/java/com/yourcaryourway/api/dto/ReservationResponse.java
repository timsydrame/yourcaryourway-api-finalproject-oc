package com.yourcaryourway.api.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private UUID id;
    private OfferResponse offer;
    private LocalDateTime departureDateTime;
    private LocalDateTime returnDateTime;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;
}