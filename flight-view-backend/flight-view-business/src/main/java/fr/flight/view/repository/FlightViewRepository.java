package fr.flight.view.repository;

import fr.flight.view.entity.Flight;
import fr.flight.view.entity.FlightView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightViewRepository extends JpaRepository<FlightView, Long>, JpaSpecificationExecutor<FlightView> {

    @Query("SELECT r FROM FlightView r " +
            "WHERE r.rating is null ")
    List<Flight> findEmptyRatings();

}
