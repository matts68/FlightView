package fr.flight.view.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Entité relative aux vols
 */
@Entity
@Table(name = "FLIGHT")
@Data
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NO_FLIGHT", nullable = false)
    private String flightNumber;

    @Column(name = "FLIGHT_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate flightDate;

    @ManyToOne
    @JoinColumn(name = "FK_DEPARTURE_AIRPORT_ID", nullable = false)
    private Airport departure;

    @ManyToOne
    @JoinColumn(name = "FK_ARRIVAL_AIRPORT_ID", nullable = false)
    private Airport arrival;

    @ManyToOne
    @JoinColumn(name = "FK_COMPANY_ID", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "flight")
    private List<FlightView> flightViews;
}
