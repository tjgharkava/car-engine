package ge.temo.carengine;

import ge.temo.carengine.cars.model.CarDTO;
import ge.temo.carengine.cars.model.CarRequest;
import ge.temo.carengine.cars.user.persistance.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static ge.temo.carengine.cars.security.AuthorizationConstants.ADMIN;
import static ge.temo.carengine.cars.security.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarsController {
    private final CarsService carsService;

    @GetMapping
    @PreAuthorize(USER_OR_ADMIN)
    public Page<CarDTO> getCars(@RequestParam int page, @RequestParam int pageSize) {
        return carsService.getCars(page, pageSize);
    }

    @GetMapping("{id}")
    @PreAuthorize(USER_OR_ADMIN)
    ResponseEntity<CarDTO> getCar(@PathVariable int id) {
        CarDTO car = carsService.findCar(id);
        if(car != null) {
            return ResponseEntity.ok(car);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize(ADMIN)
    void addCar(@RequestBody @Valid CarRequest request) {
        carsService.addCar(request);
    }

    @PutMapping("{id}")
    void updateCar(@PathVariable Long id, @RequestBody @Valid CarRequest request) {
        carsService.updateCar(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}/price")
    public ResponseEntity<String> updateCarPriceInCents(@PathVariable("id") Long carId,
                                                        @RequestParam int priceInCents) {
        try {
            carsService.updateCarPriceInCents(carId, priceInCents);
            return ResponseEntity.ok("Car price updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating car price." + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    void deleteCar(@PathVariable Long id) {
        carsService.deleteCar(id);
    }
}
