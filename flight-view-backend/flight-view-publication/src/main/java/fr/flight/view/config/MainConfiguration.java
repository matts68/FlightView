package fr.flight.view.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import fr.flight.view.FlightViewService;
import fr.flight.view.context.MapperContext;
import fr.flight.view.impl.FlightViewServiceImpl;
import fr.flight.view.mapper.FlightViewMapper;
import fr.flight.view.repository.FlightRepository;
import fr.flight.view.repository.FlightViewRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.format.DateTimeFormatter;

@Configuration
@Import({
        PersistenceContext.class,
        MapperContext.class
})
public class MainConfiguration {

    @Bean
    FlightViewService flightViewService(FlightViewRepository flightViewRepository, FlightRepository flightRepository, FlightViewMapper flightViewMapper) {
        return new FlightViewServiceImpl(flightViewRepository, flightRepository, flightViewMapper);
    }

    @Bean
    LocalDateDeserializer localDateDeserializer() {
        return new LocalDateDeserializer(DateTimeFormatter.ISO_DATE_TIME);
    }

}