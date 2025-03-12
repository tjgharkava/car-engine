package ge.temo.carengine.cars.user;

import ge.temo.carengine.CarsService;
import ge.temo.carengine.cars.model.BalanceDTO;
import ge.temo.carengine.cars.model.CarDTO;
import ge.temo.carengine.cars.persistance.Car;
import ge.temo.carengine.cars.user.model.UserRequest;
import ge.temo.carengine.cars.user.persistance.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static ge.temo.carengine.cars.security.AuthorizationConstants.ADMIN;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize(ADMIN)
public class UserController {
    private final UserService userService;
    private final CarsService carsService;

    @PostMapping
    public void createUser(@RequestBody @Valid UserRequest request) {
        userService.createUser(request);
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<String> updateBalance(@PathVariable Long id, @RequestBody @Valid BalanceDTO balanceDTO) {
        try {
            System.out.println("Updating balance for user ID: " + id);
            System.out.println("New Balance: " + balanceDTO.getBalanceInCents());
            userService.updateBalance(id, balanceDTO.getBalanceInCents());
            return ResponseEntity.ok("Successfully updated balance");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating balance");
        }
    }

    @PutMapping("/{userId}/buy/{carId}")
    public void buyCar(@PathVariable Long userId, @PathVariable Long carId) {
        userService.buyCar(userId, carId);
    }

    @PutMapping("/{userId}/sell/{carId}")
    public void sellCar(@PathVariable Long userId, @PathVariable Long carId) {
        userService.sellCar(userId, carId);
    }

    @GetMapping("/{userId}/ownedCars")
    public ResponseEntity<CarDTO> getOwnedCars(@PathVariable Long userId) {
        CarDTO carDTO = carsService.findCar(userId);
        if (carDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(carDTO);
        }
    }
}
