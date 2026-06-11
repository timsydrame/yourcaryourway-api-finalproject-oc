package com.yourcaryourway.api.seed;

import com.yourcaryourway.api.model.*;
import com.yourcaryourway.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Slf4j
@Component
@Profile("!prod")
@RequiredArgsConstructor
@Transactional
public class DataSeeder implements CommandLineRunner {

    private final AgencyRepository agencyRepository;
    private final VehicleRepository vehicleRepository;
    private final OfferRepository offerRepository;

    @Override
    public void run(String... args) {

        if (agencyRepository.count() > 0) {
            log.info("DataSeeder — données déjà présentes, seed ignoré.");
            return;
        }

        log.info("DataSeeder — insertion des données de test...");

        // AGENCES
        Agency parisCDG = agencyRepository.save(Agency.builder()
                .name("YCYW Paris Charles-de-Gaulle")
                .city("Paris")
                .country("FR")
                .address("Aéroport CDG, Terminal 2, 95700 Roissy")
                .phone("+33 1 48 62 00 00")
                .build());

        Agency parisOrly = agencyRepository.save(Agency.builder()
                .name("YCYW Paris Orly")
                .city("Paris")
                .country("FR")
                .address("Aéroport d'Orly, Terminal 4, 94390 Orly")
                .phone("+33 1 49 75 00 00")
                .build());

        Agency londonHeathrow = agencyRepository.save(Agency.builder()
                .name("YCYW London Heathrow")
                .city("London")
                .country("GB")
                .address("Heathrow Airport, Terminal 5, TW6 2GA")
                .phone("+44 20 8759 0000")
                .build());

        Agency newYorkJFK = agencyRepository.save(Agency.builder()
                .name("YCYW New York JFK")
                .city("New York")
                .country("US")
                .address("JFK International Airport, Terminal 4, NY 11430")
                .phone("+1 718 244 0000")
                .build());

        // VÉHICULES
        Vehicle renaultClio = vehicleRepository.save(Vehicle.builder()
                .brand("Renault")
                .model("Clio")
                .year(2023)
                .acrissCode("ECMR")
                .color("Blanc")
                .fuelType(Vehicle.FuelType.ESSENCE)
                .transmission(Vehicle.Transmission.MANUELLE)
                .seats(5)
                .agency(parisCDG)
                .build());

        Vehicle peugeot308 = vehicleRepository.save(Vehicle.builder()
                .brand("Peugeot")
                .model("308")
                .year(2024)
                .acrissCode("CDMR")
                .color("Gris")
                .fuelType(Vehicle.FuelType.DIESEL)
                .transmission(Vehicle.Transmission.AUTOMATIQUE)
                .seats(5)
                .agency(parisCDG)
                .build());

        Vehicle teslaModel3 = vehicleRepository.save(Vehicle.builder()
                .brand("Tesla")
                .model("Model 3")
                .year(2024)
                .acrissCode("FDAD")
                .color("Noir")
                .fuelType(Vehicle.FuelType.ELECTRIQUE)
                .transmission(Vehicle.Transmission.AUTOMATIQUE)
                .seats(5)
                .agency(parisOrly)
                .build());

        Vehicle toyotaYaris = vehicleRepository.save(Vehicle.builder()
                .brand("Toyota")
                .model("Yaris")
                .year(2023)
                .acrissCode("ECMR")
                .color("Rouge")
                .fuelType(Vehicle.FuelType.HYBRIDE)
                .transmission(Vehicle.Transmission.AUTOMATIQUE)
                .seats(5)
                .agency(londonHeathrow)
                .build());

        Vehicle fordMustang = vehicleRepository.save(Vehicle.builder()
                .brand("Ford")
                .model("Mustang")
                .year(2024)
                .acrissCode("PDAR")
                .color("Bleu")
                .fuelType(Vehicle.FuelType.ESSENCE)
                .transmission(Vehicle.Transmission.AUTOMATIQUE)
                .seats(4)
                .agency(newYorkJFK)
                .build());

        // OFFRES
        offerRepository.save(Offer.builder()
                .vehicle(renaultClio)
                .agencyDeparture(parisCDG)
                .agencyReturn(parisCDG)
                .pricePerDay(new BigDecimal("45.00"))
                .build());

        offerRepository.save(Offer.builder()
                .vehicle(peugeot308)
                .agencyDeparture(parisCDG)
                .agencyReturn(parisOrly)
                .pricePerDay(new BigDecimal("65.00"))
                .build());

        offerRepository.save(Offer.builder()
                .vehicle(teslaModel3)
                .agencyDeparture(parisOrly)
                .agencyReturn(parisCDG)
                .pricePerDay(new BigDecimal("95.00"))
                .build());

        offerRepository.save(Offer.builder()
                .vehicle(toyotaYaris)
                .agencyDeparture(londonHeathrow)
                .agencyReturn(londonHeathrow)
                .pricePerDay(new BigDecimal("55.00"))
                .build());

        offerRepository.save(Offer.builder()
                .vehicle(fordMustang)
                .agencyDeparture(newYorkJFK)
                .agencyReturn(newYorkJFK)
                .pricePerDay(new BigDecimal("120.00"))
                .build());

        log.info("DataSeeder — {} agences, {} véhicules, {} offres insérés.",
                agencyRepository.count(),
                vehicleRepository.count(),
                offerRepository.count());
    }
}