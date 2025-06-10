package fr.flight.view.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Entité relative aux companies aériennes
 */
@Entity
@Table(name = "COMPANY")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "COUNTRY_CODE", length = 2)
    private String countryCode;

    @OneToMany(mappedBy = "company")
    private List<Flight> flights;
}
