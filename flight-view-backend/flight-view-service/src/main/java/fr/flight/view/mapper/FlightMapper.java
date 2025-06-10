package fr.flight.view.mapper;

import fr.flight.view.dto.FlightDto;
import fr.flight.view.entity.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité Flight
 */
@Mapper(componentModel = "spring")
public interface FlightMapper {

    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "departureAirportName", source = "departure.name")
    @Mapping(target = "arrivalAirportName", source = "arrival.name")
    FlightDto mapEntityToDto(Flight entity);

}
