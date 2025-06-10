package fr.flight.view.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entité relative aux avis sur les vols
 */
@Entity
@Table(name = "FLIGHT_VIEW")
@Data
public class FlightView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "RATING")
    private Integer rating;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private FlightViewStatus status;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;

    @Column(name = "MUTATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modificationDate;

    @ManyToOne
    @JoinColumn(name = "FK_FLIGHT_ID", nullable = false)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "FK_CUSTOMER_ID", nullable = false)
    private Customer customer;

}
