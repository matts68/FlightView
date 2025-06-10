package fr.flight.view;

import fr.flight.view.dto.FlightViewDto;
import fr.flight.view.entity.FlightViewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface FlightViewService {

    /**
     * Recherche d'un avis en fonction de son identifiant
     * @param id identifiant de l'avis
     * @return les infos relatives à l'avis
     */
    FlightViewDto getFlightView(Long id);

    /**
     * Recherche de tous les avis
     * @param pageable éléments de pagination (incluant notamment la colonne à trier)
     * @return les infos relatives aux avis, les éléments sont paginés
     */
    Page<FlightViewDto> searchFlightViews(Pageable pageable);

    /**
     * Recherche des avis en fonction de critères
     * @param rating note de l'avis
     * @param creationDate date de création de l'avis
     * @param companyName compagnie aérienne du vol visé par l'avis
     * @param answered avis pour lesquels une réponse existe ou non (si le critère n'est pas renseigné, il n'est pas pris en compte)
     * @param pageable éléments de pagination (incluant notamment la colonne à trier)
     * @return les infos relatives aux avis visés par les critères, les éléments sont paginés
     */
    Page<FlightViewDto> searchFlightViewsByCriteria(Integer rating, LocalDate creationDate, String companyName, Boolean answered, Pageable pageable);

    /**
     * Ajout d'une réponse pour un avis
     * @param flightViewDto infos relatives à l'avis
     * @return les infos up-to-date de l'avis
     */
    FlightViewDto addAnswer(FlightViewDto flightViewDto);

    /**
     * Màj du statut pour un avis
     * @param id identifiant de l'avis
     * @param status nouveau statut de l'avis
     * @return les infos up-to-date de l'avis
     */
    FlightViewDto changeStatus(Long id, FlightViewStatus status);

}
