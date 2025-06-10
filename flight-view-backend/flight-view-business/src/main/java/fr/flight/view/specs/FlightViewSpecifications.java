package fr.flight.view.specs;

import fr.flight.view.entity.Company;
import fr.flight.view.entity.Flight;
import fr.flight.view.entity.FlightView;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

@Builder
public class FlightViewSpecifications implements Specification<FlightView> {

    private static final String EMPTY_STRING_VALUE = "";
    private static final String VIEW_ANSWER = "answer";
    private static final String VIEW_FLIGHT = "flight";
    private static final String FLIGHT_COMPANY = "company";
    private static final String COMPANY_NAME = "name";

    private final SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<FlightView> root, CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder) {
        return PredicateHelper.toPredicate(searchCriteria, root, query, criteriaBuilder);
    }

    public Specification<FlightView> filterCompany(String name) {
        return (root, query, builder) -> {
            // Jointure entre le vol et l'avis
            Join<FlightView, Flight> flight = root.join(VIEW_FLIGHT, JoinType.INNER);
            // Jointure entre la compagnie aérienne et le vol
            Join<FlightView, Company> flightCompany = flight.join(FLIGHT_COMPANY, JoinType.INNER);
            // Clause like pour filtrer les company dont le nom contient la chaîne de caractères visée
            return builder.like(
                    builder.upper(flightCompany.get(COMPANY_NAME)),
                    "%" + name.toUpperCase() + "%"
            );
        };
    }

    public Specification<FlightView> filterAnswered(Boolean answered) {
        return (root, query, builder) -> {
            // Avis ayant une réponse
            if (answered) {
                return builder.and(
                        builder.notEqual(root.get(VIEW_ANSWER), EMPTY_STRING_VALUE),
                        builder.isNotNull(root.get(VIEW_ANSWER))
                );
            }
            // Avis n'ayant pas de réponse
            return builder.or(
                    builder.equal(root.get(VIEW_ANSWER), EMPTY_STRING_VALUE),
                    builder.isNull(root.get(VIEW_ANSWER))
            );
        };
    }

}
