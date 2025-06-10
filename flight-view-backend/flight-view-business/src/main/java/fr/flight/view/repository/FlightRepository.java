package fr.flight.view.repository;

import fr.flight.view.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f " +
            "WHERE f.flightNumber = :number " +
            "AND f.flightDate = :date " +
            "AND f.company.name = :company ")
    Optional<Flight> findByNumberAndDateAndCompanyName(@Param("number") String number, @Param("date") LocalDate date, @Param("company") String company);

}
