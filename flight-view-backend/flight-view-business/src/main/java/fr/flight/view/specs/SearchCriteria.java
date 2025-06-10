package fr.flight.view.specs;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class SearchCriteria {

    private String key;
    private String operation;
    private Object value;
}