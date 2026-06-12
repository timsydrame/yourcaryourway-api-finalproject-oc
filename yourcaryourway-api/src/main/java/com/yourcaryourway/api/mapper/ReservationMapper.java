package com.yourcaryourway.api.mapper;

import com.yourcaryourway.api.dto.ReservationResponse;
import com.yourcaryourway.api.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { OfferMapper.class })
public interface ReservationMapper {

    @Mapping(target = "status", expression = "java(reservation.getStatus() != null ? reservation.getStatus().name() : null)")
    ReservationResponse toReservationResponse(Reservation reservation);
}