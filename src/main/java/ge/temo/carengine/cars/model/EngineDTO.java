package ge.temo.carengine.cars.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EngineDTO {
    private long id;
    private int horsePower;
    private double capacity;
}
