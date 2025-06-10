package fr.flight.view.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Entité relative aux aéroports
 */
@Entity
@Table(name = "AIRPORT")
@Data
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE", length = 10, nullable = false)
    private String code;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "COUNTRY_CODE", length = 2)
    private String countryCode;

    @OneToMany(mappedBy = "departure")
    private List<Flight> departureFlights;

    @OneToMany(mappedBy = "arrival")
    private List<Flight> arrivalFlights;
}
