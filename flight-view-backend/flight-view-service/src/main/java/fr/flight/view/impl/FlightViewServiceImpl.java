package fr.flight.view.impl;

import fr.flight.view.FlightViewService;
import fr.flight.view.dto.FlightViewDto;
import fr.flight.view.entity.Flight;
import fr.flight.view.entity.FlightView;
import fr.flight.view.entity.FlightViewStatus;
import fr.flight.view.exception.FlightViewNotFoundException;
import fr.flight.view.mapper.FlightViewMapper;
import fr.flight.view.repository.FlightRepository;
import fr.flight.view.repository.FlightViewRepository;
import fr.flight.view.specs.FlightViewSpecifications;
import fr.flight.view.specs.SearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class FlightViewServiceImpl implements FlightViewService {

    private final FlightViewRepository flightViewRepository;
    private final FlightRepository flightRepository;
    private final FlightViewMapper flightViewMapper;

    @Override
    @Transactional(readOnly = true)
    public FlightViewDto getFlightView(Long id) {
        // Recherche de l'avis par son id
        FlightView flightView = findFlightViewById(id);
        // Mapping entité -> dto
        return flightViewMapper.mapEntityToDto(flightView);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightViewDto> searchFlightViews(Pageable pageable) {
        // Recherche de tous les avis (triés suivant l'information transmise par le paramètre "pageable")
        return flightViewRepository.findAll(sortPageable(pageable))
                // Application du mapping entité -> dto
                .map(flightViewMapper::mapEntityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightViewDto> searchFlightViewsByCriteria(Integer rating, LocalDate creationDate, String companyName, Boolean answered, Pageable pageable) {

        List<Specification<FlightView>> specificationList = new ArrayList<>();

        // Filtre des avis sur la note (critère optionnel)
        if (Objects.nonNull(rating)) {
            specificationList.add(
                    FlightViewSpecifications.builder()
                            .searchCriteria(SearchCriteria.builder()
                                    .key("rating")
                                    .operation(":")
                                    .value(rating)
                                    .build())
                            .build()
            );
        }

        // Filtre des avis sur la date (critère optionnel)
        if (Objects.nonNull(creationDate)) {
            specificationList.add(
                    FlightViewSpecifications.builder()
                            .searchCriteria(SearchCriteria.builder()
                                    .key("creationDate")
                                    .operation(":")
                                    .value(creationDate)
                                    .build())
                            .build()
            );
        }

        // Filtre des avis sur la compagnie aérienne (critère optionnel)
        if (StringUtils.hasText(companyName)) {
            specificationList.add(
                    FlightViewSpecifications.builder()
                            .build()
                            .filterCompany(companyName)
            );
        }

        // Filtre des avis sur ceux sans réponse (critère optionnel)
        if (Objects.nonNull(answered)) {
            specificationList.add(
                    FlightViewSpecifications.builder()
                        .build()
                        .filterAnswered(answered)
            );
        }

        if (CollectionUtils.isEmpty(specificationList)) {
            // Aucun critère : appel au service de recherche de tous les avis
            return searchFlightViews(pageable);
        }
        // Filtre des avis conformément aux crtères saisis
        // Tri des avis suivant l'information transmise par le paramètre "pageable"
        Page<FlightView> pageResults = flightViewRepository.findAll(
                Specification.allOf(specificationList),
                sortPageable(pageable));
        // Application du mapping entité -> dto
        return pageResults.map(flightViewMapper::mapEntityToDto);
    }

    @Override
    @Transactional
    public FlightViewDto addAnswer(FlightViewDto flightViewDto) {
        FlightView flightView = findFlightViewById(flightViewDto.getId());
        flightView.setAnswer(flightViewDto.getAnswer());
        return flightViewMapper.mapEntityToDto(flightViewRepository.save(flightView));
    }

    @Override
    @Transactional
    public FlightViewDto changeStatus(Long id, FlightViewStatus status) {
        FlightView flightView = findFlightViewById(id);
        flightView.setStatus(status);
        return flightViewMapper.mapEntityToDto(flightViewRepository.save(flightView));
    }

    private FlightView findFlightViewById(Long id) {
        return flightViewRepository.findById(id)
                .orElseThrow(() -> new FlightViewNotFoundException("Avis non trouvé"));
    }

    private Pageable sortPageable(Pageable pageable) {
        // Contrôle de la propriété à trier pour qu'elle soit conforme aux champs de l'entité

        Sort.Order sortOrder = pageable.getSort() != null ? pageable.getSort().stream().findFirst().orElse(null) : null;
        String property = sortOrder != null ? sortOrder.getProperty() : null;

        // Seules les propriétés ci-après sont autorisées pour le tri
        if ("rating".equalsIgnoreCase(property)) {
            property = "rating";
        } else if ("date".equalsIgnoreCase(property)) {
            property = "creationDate";
        } else if ("company".equalsIgnoreCase(property)) {
            property = "company.name";
        }

        if (StringUtils.hasText(property)) {
            return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    sortOrder.isAscending() ? Sort.by(property).ascending() : Sort.by(property).descending());
        }

        // Par défaut tri des demandes les plus récentes
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("creationDate").descending());
    }

}
