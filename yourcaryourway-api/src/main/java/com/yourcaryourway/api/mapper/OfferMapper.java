package com.yourcaryourway.api.mapper;

import com.yourcaryourway.api.dto.AgencyResponse;
import com.yourcaryourway.api.dto.OfferResponse;
import com.yourcaryourway.api.dto.VehicleResponse;
import com.yourcaryourway.api.model.Agency;
import com.yourcaryourway.api.model.Offer;
import com.yourcaryourway.api.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    @Mapping(target = "fuelType", expression = "java(vehicle.getFuelType().name())")
    @Mapping(target = "transmission", expression = "java(vehicle.getTransmission().name())")
    VehicleResponse toVehicleResponse(Vehicle vehicle);

    AgencyResponse toAgencyResponse(Agency agency);

    OfferResponse toOfferResponse(Offer offer);
}