package com.yourcaryourway.api.repository;

import com.yourcaryourway.api.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AgencyRepository extends JpaRepository<Agency, UUID> {

    @Query("""
        SELECT a FROM Agency a
        WHERE (:city    IS NULL OR LOWER(a.city)    = LOWER(:city))
          AND (:country IS NULL OR LOWER(a.country) = LOWER(:country))
        ORDER BY a.country, a.city, a.name
    """)
    List<Agency> searchAgencies(
            @Param("city")    String city,
            @Param("country") String country
    );
}