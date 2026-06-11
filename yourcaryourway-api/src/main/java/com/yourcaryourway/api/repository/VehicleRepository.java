package com.yourcaryourway.api.repository;

import com.yourcaryourway.api.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    @Query("""
        SELECT v FROM Vehicle v
        JOIN FETCH v.agency a
        WHERE v.available = true
          AND (:acrissCode IS NULL OR UPPER(v.acrissCode) = UPPER(:acrissCode))
        ORDER BY v.brand, v.model
    """)
    List<Vehicle> searchAvailableVehicles(
            @Param("acrissCode") String acrissCode
    );
}