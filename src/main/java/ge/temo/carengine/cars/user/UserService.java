package ge.temo.carengine.cars.user;

import ge.temo.carengine.cars.error.NotFoundException;
import ge.temo.carengine.cars.persistance.Car;
import ge.temo.carengine.cars.persistance.CarRepository;
import ge.temo.carengine.cars.user.persistance.AppUser;
import ge.temo.carengine.cars.user.persistance.AppUserRepository;
import ge.temo.carengine.cars.user.model.UserRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;



    public void createUser(UserRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(request.getRoleIds().stream()
                .map(roleService::getRole)
                .collect(Collectors.toSet()));

        repository.save(user);
    }

    public AppUser getUser(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void updateBalance(Long userId, int balance) {
        AppUser user = repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        user.setBalanceInCents(balance);
        repository.save(user);
    }
}
