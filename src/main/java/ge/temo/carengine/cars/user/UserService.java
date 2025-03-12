package ge.temo.carengine.cars.user;

import ge.temo.carengine.CarsService;
import ge.temo.carengine.cars.error.NotFoundException;
import ge.temo.carengine.cars.model.CarDTO;
import ge.temo.carengine.cars.persistance.Car;
import ge.temo.carengine.cars.persistance.CarRepository;
import ge.temo.carengine.cars.user.persistance.AppUser;
import ge.temo.carengine.cars.user.persistance.AppUserRepository;
import ge.temo.carengine.cars.user.model.UserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final CarRepository carRepository;


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

    public void buyCar(Long userId, Long carId) {
        AppUser user = repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        if (user.getBalanceInCents() >= car.getPriceInCents()) {
            user.setBalanceInCents(user.getBalanceInCents() - car.getPriceInCents());
            user.getOwnedCars().add(car);
            repository.save(user);
        } else {
            throw new RuntimeException("Insufficient funds");
        }
    }

    public void sellCar(Long userId, Long carId) {
        AppUser user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        if (user.getOwnedCars().contains(car)) {
            user.setBalanceInCents(user.getBalanceInCents() + (int) (car.getPriceInCents() * 0.8));
            user.getOwnedCars().remove(car);
            repository.save(user);
        } else {
            throw new RuntimeException("Car not owned");
        }
    }

    public Set<Car> getOwnedCars(Long userId) {
        AppUser user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user != null ? user.getOwnedCars() : null;
    }
}
