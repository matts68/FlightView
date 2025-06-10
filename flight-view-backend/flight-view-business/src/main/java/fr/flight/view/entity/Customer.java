package fr.flight.view.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Entité relative aux clients (passagers) des vols
 */
@Entity
@Table(name = "CUSTOMER")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FIRST_NAME", length = 100, nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", length = 100, nullable = false)
    private String lastName;

    @Column(name = "BIRTH_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "customer")
    private List<FlightView> flightViews;

}
