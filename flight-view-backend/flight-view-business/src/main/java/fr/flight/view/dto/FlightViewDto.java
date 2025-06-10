package fr.flight.view.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class FlightViewDto {

    private final Long id;
    private final Integer rating;
    private final String remarks;
    private final String answer;
    private final String status;
    private final LocalDateTime creationDate;
    private final FlightDto flight;
    private final CustomerDto customer;

}
