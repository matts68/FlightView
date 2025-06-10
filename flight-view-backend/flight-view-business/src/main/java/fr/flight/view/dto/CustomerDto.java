package fr.flight.view.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerDto {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;

}
