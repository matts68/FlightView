package fr.flight.view.mapper;

import fr.flight.view.dto.CustomerDto;
import fr.flight.view.entity.Customer;
import org.mapstruct.Mapper;

/**
 * Mapper pour l'entité Customer
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto mapEntityToDto(Customer entity);

}
