package com.yourcaryourway.api.service;

import com.yourcaryourway.api.dto.OfferResponse;
import com.yourcaryourway.api.dto.OfferSearchRequest;
import com.yourcaryourway.api.mapper.OfferMapper;
import com.yourcaryourway.api.repository.OfferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public List<OfferResponse> searchOffers(OfferSearchRequest request) {
        String departureCity = normalize(request.getDepartureCity());
        String returnCity = normalize(request.getReturnCity());
        String acrissCode = normalizeUpper(request.getAcrissCode());

        return offerRepository
                .searchOffers(departureCity, returnCity, acrissCode)
                .stream()
                .map(offerMapper::toOfferResponse)
                .toList();
    }

    public OfferResponse findById(UUID id) {
        return offerRepository.findById(id)
                .map(offerMapper::toOfferResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Offre introuvable : " + id
                ));
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizeUpper(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim().toUpperCase();
    }
}