package com.yourcaryourway.api.controller;

import com.yourcaryourway.api.dto.CreateReservationRequest;
import com.yourcaryourway.api.dto.ReservationResponse;
import com.yourcaryourway.api.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Validated
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody CreateReservationRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservationService.create(request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ReservationResponse>> getMyReservations() {
        return ResponseEntity.ok(reservationService.getMyReservations());
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponse> cancel(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(reservationService.cancel(id));
    }
}