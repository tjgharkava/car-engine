package ge.temo.carengine.cars;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FuelEfficiencyCalculatorTest {

    private final FuelEfficiencyCalculator fuelEfficiencyCalculator = new FuelEfficiencyCalculator();

    @Test
    void shouldCalculateFuelEfficiencyCorrectly() {
        double horsePower = 400;
        double capacity = 4.4;
        double weightKg = 1800;

        double expectedResult = 0.8;
        double actualResult = fuelEfficiencyCalculator.calculateFuelEfficiency(horsePower, capacity, weightKg);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void shouldThrowExceptionInvalidHorsePower() {
        double horsePower = -1;
        double capacity = 4.4;
        double weightKg = 1800;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> fuelEfficiencyCalculator.calculateFuelEfficiency(horsePower, capacity, weightKg));
    }

    @Test
    void shouldThrowExceptionInvalidCapacity() {
        double horsePower = 400;
        double capacity = -1;
        double weightKg = 1800;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> fuelEfficiencyCalculator.calculateFuelEfficiency(horsePower, capacity, weightKg));
    }

    @Test
    void shouldthrowExceptionInvalidWeightKg() {
        double horsePower = 400;
        double capacity = 4.4;
        double weightKg = -1;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> fuelEfficiencyCalculator.calculateFuelEfficiency(horsePower, capacity, weightKg));
    }
}
