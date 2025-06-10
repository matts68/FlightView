package fr.flight.view.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FlightViewCreationDto {

    private final String flightNumber;
    private final LocalDate flightDate;
    private final String company;
    private final Integer rating;
    private final String remarks;

}
