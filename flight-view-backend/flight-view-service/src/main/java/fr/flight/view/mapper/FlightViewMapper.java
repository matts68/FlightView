package fr.flight.view.mapper;

import fr.flight.view.dto.FlightViewDto;
import fr.flight.view.entity.FlightView;
import fr.flight.view.entity.FlightViewStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper pour l'entité Flight
 */
@Mapper(componentModel = "spring", uses = { FlightMapper.class, CustomerMapper.class })
public interface FlightViewMapper {

    FlightViewDto mapEntityToDto(FlightView entity);

    List<FlightViewDto> mapEntityListToDtoList(List<FlightView> entityList);

    @Mapping(target = "flight", ignore = true)
    @Mapping(target = "customer", ignore = true)
    FlightView mapDtoToEntity(FlightViewDto entity);

    default FlightViewStatus mapToFlightViewStatus(String value) {
        FlightViewStatus result = null;
        try {
            return FlightViewStatus.valueOf(value);
        } catch (RuntimeException e) {
            return null;
        }
    }

    default String mapToStringValue(FlightViewStatus status) {
        return status != null ? status.name() : null;
    }

}
