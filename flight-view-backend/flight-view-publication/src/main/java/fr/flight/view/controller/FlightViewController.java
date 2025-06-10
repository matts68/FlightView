package fr.flight.view.controller;

import fr.flight.view.FlightViewService;
import fr.flight.view.dto.FlightViewCreationDto;
import fr.flight.view.dto.FlightViewDto;
import fr.flight.view.entity.FlightViewStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class FlightViewController {

    private final FlightViewService flightViewService;

    @GetMapping(value = "/views")
    @ResponseBody
    public ResponseEntity<Page<FlightViewDto>> search(
            @RequestParam Optional<Integer> rating,
            @RequestParam Optional<String> company,
            @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") Optional<LocalDate> date,
            @RequestParam Optional<Boolean> answered,
            Pageable p) {

        // Recherche des demandes conformément à la pagination souhaitée
        Pageable pageable = PageRequest.of(
                p.getPageNumber(),
                Optional.ofNullable(p.getPageSize()).orElse(1000), // Limite par défaut
                p.getSort());
        Page<FlightViewDto> searchResults = flightViewService.searchFlightViewsByCriteria(
                rating.orElse(null),
                date.orElse(null),
                company.orElse(null),
                answered.orElse(null),
                pageable);
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    @GetMapping(value = "/view/{id}")
    @ResponseBody
    public ResponseEntity<FlightViewDto> getView(@PathVariable Long id) {

        // Recherche d'un avis par son identifiant
        FlightViewDto flightView = flightViewService.getFlightView(id);
        return new ResponseEntity<>(flightView, HttpStatus.OK);
    }

    @PutMapping(value = "/view/answer")
    @ResponseBody
    public ResponseEntity<FlightViewDto> putAnswer(@RequestBody FlightViewDto flightView) {

        // Màj de la réponse pour un avis
        FlightViewDto flightViewResult = flightViewService.addAnswer(flightView);
        return new ResponseEntity<>(flightViewResult, HttpStatus.OK);
    }

    @PutMapping(value = "/view/{id}/processed")
    @ResponseBody
    public ResponseEntity<FlightViewDto> markAsProcessed(@PathVariable Long id) {

        // Avis marqué comme traité
        FlightViewDto flightViewResult = flightViewService.changeStatus(id, FlightViewStatus.PROCESSED);
        return new ResponseEntity<>(flightViewResult, HttpStatus.OK);
    }

    @PutMapping(value = "/view/{id}/published")
    @ResponseBody
    public ResponseEntity<FlightViewDto> markAsPublished(@PathVariable Long id) {

        // Avis marqué comme publié
        FlightViewDto flightViewResult = flightViewService.changeStatus(id, FlightViewStatus.PUBLISHED);
        return new ResponseEntity<>(flightViewResult, HttpStatus.OK);
    }

    @PutMapping(value = "/view/{id}/rejected")
    @ResponseBody
    public ResponseEntity<FlightViewDto> markAsRejected(@PathVariable Long id) {

        // Avis marqué comme rejeté
        FlightViewDto flightViewResult = flightViewService.changeStatus(id, FlightViewStatus.REJECTED);
        return new ResponseEntity<>(flightViewResult, HttpStatus.OK);
    }

    @PostMapping(value = "/view")
    @ResponseBody
    public ResponseEntity<FlightViewDto> create(@RequestBody FlightViewCreationDto flightView) {

        // TODO à compléter
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
