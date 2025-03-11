package ge.temo.carengine.cars.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarRequest {
    @NotBlank
    @Size(max = 20)
    private String model;
    @Min(1940)
    private int year;
    private boolean driveable;
    @Positive
    private Long engineId;
}

