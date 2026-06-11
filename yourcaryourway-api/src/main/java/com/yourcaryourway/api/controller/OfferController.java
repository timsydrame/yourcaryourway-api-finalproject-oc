package com.yourcaryourway.api.controller;

import com.yourcaryourway.api.dto.OfferResponse;
import com.yourcaryourway.api.dto.OfferSearchRequest;
import com.yourcaryourway.api.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
@Validated
public class OfferController {

    private final OfferService offerService;

    @PostMapping("/search")
    public ResponseEntity<List<OfferResponse>> searchOffers(
            @Valid @RequestBody OfferSearchRequest request
    ) {
        return ResponseEntity.ok(offerService.searchOffers(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(offerService.findById(id));
    }
}