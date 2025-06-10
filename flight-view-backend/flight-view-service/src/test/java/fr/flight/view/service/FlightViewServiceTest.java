package fr.flight.view.service;

import fr.flight.view.dto.CustomerDto;
import fr.flight.view.dto.FlightDto;
import fr.flight.view.dto.FlightViewDto;
import fr.flight.view.entity.FlightView;
import fr.flight.view.entity.FlightViewStatus;
import fr.flight.view.exception.FlightViewNotFoundException;
import fr.flight.view.impl.FlightViewServiceImpl;
import fr.flight.view.mapper.FlightViewMapper;
import fr.flight.view.repository.FlightRepository;
import fr.flight.view.repository.FlightViewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightViewServiceTest {

    @InjectMocks
    private FlightViewServiceImpl service;

    @Mock
    FlightViewRepository flightViewRepository;

    @Mock
    FlightRepository flightRepository;

    @Mock
    FlightViewMapper flightViewMapper;

    @Test
    void getFlightView_successful_with_valid_id() throws Exception {
        // Given
        Long id = Long.valueOf(1);
        FlightView flightView = new FlightView();
        FlightViewDto flightViewDto = buildFlightViewDto(id);

        when(flightViewRepository.findById(any())).thenReturn(Optional.of(flightView));
        when(flightViewMapper.mapEntityToDto(any())).thenReturn(flightViewDto);

        // When
        FlightViewDto result = service.getFlightView(id);

        // Then
        assertEquals(result.getId(), id);
        verify(flightViewMapper, times(1)).mapEntityToDto(any());
    }

    @Test
    void getFlightView_failure_with_invalid_id() throws Exception {
        // Given
        Long id = Long.valueOf(-1);
        FlightView flightView = new FlightView();
        FlightViewDto flightViewDto = buildFlightViewDto(id);

        when(flightViewRepository.findById(any())).thenReturn(Optional.empty());

        // When && Then
        assertThrows(FlightViewNotFoundException.class, () -> {
            service.getFlightView(id);
        });
    }

    private FlightDto buildFlightDto() {
        return new FlightDto(1L, "11111", LocalDate.now(), "Air France", "Nice", "Brest");
    }

    private CustomerDto buildCustomerDto() {
        return new CustomerDto(1L, "John", "Doe", LocalDate.of(1956, 2, 5));
    }

    private FlightViewDto buildFlightViewDto(Long id) {
        return new FlightViewDto(id, 4, "OK", null, FlightViewStatus.PUBLISHED.name(), LocalDateTime.now(), buildFlightDto(), buildCustomerDto());
    }

}
