package fr.flight.view.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FlightDto {

    private final Long id;
    private final String flightNumber;
    private final LocalDate flightDate;
    private final String companyName;
    private final String departureAirportName;
    private final String arrivalAirportName;

}
