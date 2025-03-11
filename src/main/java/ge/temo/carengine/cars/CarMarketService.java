package ge.temo.carengine.cars;

import ge.temo.carengine.cars.persistance.Car;
import ge.temo.carengine.cars.persistance.CarRepository;
import ge.temo.carengine.cars.user.persistance.AppUser;
import ge.temo.carengine.cars.user.persistance.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CarMarketService {
    private final AppUserRepository userRepository;
    private final CarRepository carRepository;

    public void buyCar(Long userId, Long carId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (user.getBalanceInCents() < car.getPriceInCents()) {
            throw new RuntimeException("Insufficient balance to buy this car.");
        }

        if (user.getOwnedCars().contains(car)) {
            throw new RuntimeException("User already owns this car.");
        }

        user.setBalanceInCents((int)(user.getBalanceInCents() - car.getPriceInCents()));
        user.getOwnedCars().add(car);

        userRepository.save(user);
    }

    public void sellCar(Long userId, Long carId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!user.getOwnedCars().contains(car)) {
            throw new RuntimeException("User does not own this car.");
        }

        int refundAmount = (int) (car.getPriceInCents() * 0.8);
        user.setBalanceInCents(user.getBalanceInCents() + refundAmount);
        user.getOwnedCars().remove(car);

        userRepository.save(user);
    }

    public Set<Car> getOwnedCars(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getOwnedCars();
    }
}
