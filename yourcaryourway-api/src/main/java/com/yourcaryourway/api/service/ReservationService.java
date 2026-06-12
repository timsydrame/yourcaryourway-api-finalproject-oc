package com.yourcaryourway.api.service;

import com.yourcaryourway.api.dto.CreateReservationRequest;
import com.yourcaryourway.api.dto.ReservationResponse;
import com.yourcaryourway.api.mapper.ReservationMapper;
import com.yourcaryourway.api.model.Offer;
import com.yourcaryourway.api.model.Reservation;
import com.yourcaryourway.api.model.User;
import com.yourcaryourway.api.repository.OfferRepository;
import com.yourcaryourway.api.repository.ReservationRepository;
import com.yourcaryourway.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public ReservationResponse create(CreateReservationRequest request) {
        User user = getConnectedUser();

        Offer offer = offerRepository.findById(request.getOfferId())
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable : " + request.getOfferId()));

        if (!offer.isAvailable()) {
            throw new IllegalStateException("Cette offre n'est plus disponible");
        }

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                offer.getId(),
                request.getDepartureDateTime(),
                request.getReturnDateTime()
        );

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Ce véhicule est déjà réservé sur ces dates");
        }

        long days = ChronoUnit.DAYS.between(
                request.getDepartureDateTime().toLocalDate(),
                request.getReturnDateTime().toLocalDate()
        );

        if (days < 1) {
            days = 1;
        }

        BigDecimal totalPrice = offer.getPricePerDay()
                .multiply(BigDecimal.valueOf(days))
                .setScale(2, RoundingMode.HALF_UP);

        Reservation reservation = Reservation.builder()
                .user(user)
                .offer(offer)
                .departureDateTime(request.getDepartureDateTime())
                .returnDateTime(request.getReturnDateTime())
                .totalPrice(totalPrice)
                .status(Reservation.Status.CONFIRMED)
                .build();

        return reservationMapper.toReservationResponse(
                reservationRepository.save(reservation)
        );
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getMyReservations() {
        User user = getConnectedUser();

        return reservationRepository.findHistoryByUserId(user.getId())
                .stream()
                .map(reservationMapper::toReservationResponse)
                .toList();
    }

    @Transactional
    public ReservationResponse cancel(UUID reservationId) {
        User user = getConnectedUser();

        Reservation reservation = reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Réservation introuvable : " + reservationId));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Vous n'êtes pas autorisé à annuler cette réservation");
        }

        if (reservation.getStatus() == Reservation.Status.CANCELLED) {
            throw new IllegalStateException("Cette réservation est déjà annulée");
        }

        if (!LocalDateTime.now().isBefore(reservation.getDepartureDateTime())) {
            throw new IllegalStateException("Annulation impossible : la réservation a déjà commencé");
        }

        reservation.setStatus(Reservation.Status.CANCELLED);

        return reservationMapper.toReservationResponse(
                reservationRepository.save(reservation)
        );
    }

    private User getConnectedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
    }
}