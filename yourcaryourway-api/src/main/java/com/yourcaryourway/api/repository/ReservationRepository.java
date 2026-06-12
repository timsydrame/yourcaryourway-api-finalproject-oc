package com.yourcaryourway.api.repository;

import com.yourcaryourway.api.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query("""
        SELECT r FROM Reservation r
        JOIN FETCH r.offer o
        JOIN FETCH o.vehicle v
        JOIN FETCH o.agencyDeparture ad
        JOIN FETCH o.agencyReturn ar
        WHERE r.user.id = :userId
        ORDER BY r.createdAt DESC
    """)
    List<Reservation> findHistoryByUserId(@Param("userId") UUID userId);

    @Query("""
        SELECT r FROM Reservation r
        JOIN FETCH r.offer o
        JOIN FETCH o.vehicle v
        JOIN FETCH o.agencyDeparture ad
        JOIN FETCH o.agencyReturn ar
        JOIN FETCH r.user u
        WHERE r.id = :id
    """)
    Optional<Reservation> findByIdWithDetails(@Param("id") UUID id);

    @Query("""
        SELECT r FROM Reservation r
        WHERE r.offer.id = :offerId
          AND r.status IN ('PENDING', 'CONFIRMED', 'MODIFIED')
          AND r.departureDateTime < :returnDateTime
          AND r.returnDateTime > :departureDateTime
    """)
    List<Reservation> findConflictingReservations(
            @Param("offerId") UUID offerId,
            @Param("departureDateTime") LocalDateTime departureDateTime,
            @Param("returnDateTime") LocalDateTime returnDateTime
    );
}