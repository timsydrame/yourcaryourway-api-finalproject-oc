package com.yourcaryourway.api.repository;

import com.yourcaryourway.api.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {

    @Query("""
        SELECT o FROM Offer o
        JOIN FETCH o.vehicle v
        JOIN FETCH o.agencyDeparture ad
        JOIN FETCH o.agencyReturn ar
        WHERE o.available = true
          AND v.available = true
          AND (:departureCity IS NULL OR LOWER(ad.city) = LOWER(:departureCity))
          AND (:returnCity IS NULL OR LOWER(ar.city) = LOWER(:returnCity))
          AND (:acrissCode IS NULL OR UPPER(v.acrissCode) = UPPER(:acrissCode))
    """)
    List<Offer> searchOffers(
            @Param("departureCity") String departureCity,
            @Param("returnCity") String returnCity,
            @Param("acrissCode") String acrissCode
    );
}