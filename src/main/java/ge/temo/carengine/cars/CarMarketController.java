package ge.temo.carengine.cars;

import ge.temo.carengine.cars.persistance.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
public class CarMarketController {
    private final CarMarketService carMarketService;

    @PostMapping("/buy")
    public ResponseEntity<String> buyCar(@RequestParam Long userId, @RequestParam Long carId) {
        carMarketService.buyCar(userId, carId);
        return ResponseEntity.ok("Car purchased successfully");
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellCar(@RequestParam Long userId, @RequestParam Long carId) {
        carMarketService.sellCar(userId, carId);
        return ResponseEntity.ok("Car sold successfully");
    }

    @GetMapping("/owned-cars")
    public ResponseEntity<Set<Car>> getOwnedCars(@PathVariable Long userId) {
        Set<Car> ownedCars = carMarketService.getOwnedCars(userId);
        return ResponseEntity.ok(ownedCars);
    }
}
