package ge.temo.carengine;

import ge.temo.carengine.cars.model.CarDTO;
import ge.temo.carengine.cars.model.CarRequest;
import ge.temo.carengine.cars.persistance.Car;
import ge.temo.carengine.cars.user.persistance.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping("/{carId}/image")
    public ResponseEntity<CarDTO> setCarImage(@PathVariable Long carId, @RequestParam String imageUrl)
    {
        CarDTO car = carsService.updateCarImage(carId, imageUrl);
        return ResponseEntity.ok(car);
    }


    @DeleteMapping("{id}")
    void deleteCar(@PathVariable Long id) {
        carsService.deleteCar(id);
    }
}
