package ge.temo.carengine.cars.user;

import ge.temo.carengine.cars.model.BalanceDTO;
import ge.temo.carengine.cars.user.model.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static ge.temo.carengine.cars.security.AuthorizationConstants.ADMIN;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize(ADMIN)
public class UserController {
    private final UserService userService;

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
}
