package com.yourcaryourway.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReservationRequest {

    @NotNull(message = "L'offre est obligatoire")
    private UUID offerId;

    @NotNull(message = "La date de départ est obligatoire")
    @Future(message = "La date de départ doit être dans le futur")
    private LocalDateTime departureDateTime;

    @NotNull(message = "La date de retour est obligatoire")
    @Future(message = "La date de retour doit être dans le futur")
    private LocalDateTime returnDateTime;

    @AssertTrue(message = "La date de retour doit être après la date de départ")
    public boolean isReturnDateTimeAfterDepartureDateTime() {
        if (departureDateTime == null || returnDateTime == null) {
            return true;
        }
        return returnDateTime.isAfter(departureDateTime);
    }
}