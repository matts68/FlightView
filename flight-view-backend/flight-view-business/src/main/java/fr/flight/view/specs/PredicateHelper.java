package fr.flight.view.specs;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.tree.from.SqmRoot;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Slf4j
public class PredicateHelper {

    private static boolean isAnnotedWith(final Root<?> root, final String field, final Class classToTest) {
        try {
            return Objects.nonNull(
                ((SqmRoot) root).getJavaType().getDeclaredField(field).getAnnotation(classToTest));
        } catch (NoSuchFieldException e) {
            log.error("cannot find field " + field, e);
            return false;
        }
    }

    public static Predicate toPredicate(SearchCriteria searchCriteria, Root<?> root,
        CriteriaQuery<?> query, CriteriaBuilder builder) {

        final String key = searchCriteria.getKey();
        Path path = null;

        if (key.contains(".")) {
            /* joining cases */
            final String[] split = key.split("\\.");
            final String firstLevel = split[0];
            final String secondLevel = split[1];

            if (isAnnotedWith(root, firstLevel, ManyToMany.class)
                || isAnnotedWith(root, firstLevel, OneToMany.class)
                || isAnnotedWith(root, firstLevel, ManyToOne.class)) {
                path = root.join(firstLevel).get(secondLevel);
            } else if (isAnnotedWith(root, firstLevel, OneToOne.class)) {
                path = root.get(firstLevel).get(secondLevel);
            }
        } else {
            path = root.get(key);
        }

        if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
            /* simply > */
            return builder.greaterThanOrEqualTo(
                builder.lower(path), searchCriteria.getValue().toString().toLowerCase());
        } else if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
            /* simply < */
            return builder.lessThanOrEqualTo(
                builder.lower(path), searchCriteria.getValue().toString().toLowerCase());
        } else if (searchCriteria.getOperation().equalsIgnoreCase(":")) {
            /* standard string search with like %TOTO% */
            if (path.getJavaType() == String.class) {
                return builder.like(
                    builder.lower(path), "%" + searchCriteria.getValue().toString().toLowerCase() + "%");
            } else if (path.getJavaType() == LocalDateTime.class) {
                return toPredicate((LocalDate) searchCriteria.getValue(), builder, path);
            }  else if (path.getJavaType() == LocalDate.class) {
                return toPredicate((LocalDate) searchCriteria.getValue(), builder, path);
            } else if (path.getJavaType() == Instant.class) {
                return toPredicate((LocalDate) searchCriteria.getValue(), builder, path);
            } else {
                /* brutal comparaison ! */
                if (searchCriteria.getValue() instanceof String) {
                    return builder.equal(builder.lower(path), searchCriteria.getValue().toString().toLowerCase());
                } else {
                    return builder.equal(path, searchCriteria.getValue());
                }

            }
        }

        return null;
    }

    private static Predicate toPredicate(LocalDateTime value, CriteriaBuilder builder, Path path) {
        /* equals date time case. we make a between min and max date of the day that we compare */
        LocalDateTime dateBegin = zeroTime(value);
        LocalDateTime dateEnd = maxDateDuringDayDate(value);
        return builder.between(path, dateBegin, dateEnd);
    }

    private static Predicate toPredicate(LocalDate value, CriteriaBuilder builder, Path path) {
        /* equals date case. we make a between min and max instant of the day that we compare */
        Instant dateBegin = zeroTime(value);
        Instant dateEnd = maxDateDuringDayDate(value);
        return builder.between(path, dateBegin, dateEnd);
    }

    private static LocalDateTime zeroTime(final LocalDateTime date) {
        return date.toLocalDate().atTime(0, 0);
    }

    private static Instant zeroTime(final LocalDate date) {
        return date.atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    private static LocalDateTime maxDateDuringDayDate(final LocalDateTime date) {
        return date.toLocalDate().atTime(23, 59);
    }

    private static Instant maxDateDuringDayDate(final LocalDate date) {
        return date.atTime(23, 59).toInstant(ZoneOffset.UTC);
    }

}
