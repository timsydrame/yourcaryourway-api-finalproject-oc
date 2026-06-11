package com.yourcaryourway.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OfferSearchRequest {

    @NotBlank(message = "La ville de départ est obligatoire")
    @Size(max = 100, message = "La ville de départ ne doit pas dépasser 100 caractères")
    private String departureCity;

    @NotBlank(message = "La ville de retour est obligatoire")
    @Size(max = 100, message = "La ville de retour ne doit pas dépasser 100 caractères")
    private String returnCity;

    @NotNull(message = "La date de départ est obligatoire")
    @Future(message = "La date de départ doit être dans le futur")
    private LocalDateTime departureDateTime;

    @NotNull(message = "La date de retour est obligatoire")
    @Future(message = "La date de retour doit être dans le futur")
    private LocalDateTime returnDateTime;

    @Pattern(
            regexp = "^[A-Z]{4}$",
            message = "Le code ACRISS doit contenir 4 lettres majuscules, ex: ECMR"
    )
    private String acrissCode;

    @AssertTrue(message = "La date de retour doit être après la date de départ")
    public boolean isReturnDateTimeAfterDepartureDateTime() {
        if (departureDateTime == null || returnDateTime == null) return true;
        return returnDateTime.isAfter(departureDateTime);
    }
}