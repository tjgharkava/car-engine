package ge.temo.carengine.cars.persistance;

import ge.temo.carengine.cars.model.EngineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EngineRepository extends JpaRepository<Engine, Long> {

    @Query("SELECT NEW ge.temo.carengine.cars.model.EngineDTO(e.id, e.horsePower, e.capacity)" +
            " FROM Engine e WHERE e.capacity = :capacity")
    Page<EngineDTO> findEngines(double capacity, Pageable pageable);
}
