package ge.temo.carengine;

import ge.temo.carengine.cars.error.NotFoundException;
import ge.temo.carengine.cars.model.CarDTO;
import ge.temo.carengine.cars.model.CarRequest;
import ge.temo.carengine.cars.model.EngineDTO;
import ge.temo.carengine.cars.persistance.Car;
import ge.temo.carengine.cars.persistance.CarRepository;
import ge.temo.carengine.cars.user.persistance.AppUser;
import ge.temo.carengine.cars.user.persistance.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarsService {
    private final CarRepository carRepository;
    private final EngineService engineService;
    private final AppUserRepository userRepository;


    public Page<CarDTO> getCars(int page, int pageSize) {
        return carRepository.findCars(PageRequest.of(page, pageSize));
    }

    public void addCar(CarRequest request) {
        Car car = new Car();
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setDriveable(request.isDriveable());
        car.setEngine(engineService.findEngine(request.getEngineId()));
        carRepository.save(car);
    }

    public CarDTO findCar(long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
        return mapCar(car);
    }

    private NotFoundException buildNotFoundException(long id) {
        return new NotFoundException("Car with id " + id + " not found");
    }

    public void updateCar(Long id, CarRequest request) {
        Car car = carRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public Car setCarImage(Long carId, String imageUrl) {
        Car car = carRepository.findById(carId).orElseThrow(() -> buildNotFoundException(carId));
        if (car != null) {
            car.setImageUrl(imageUrl);
            return carRepository.save(car);
        }
        return null;
    }

    public CarDTO updateCarImage(Long CarId, String imageUrl) {
        Car car = carRepository.findById(CarId).orElseThrow(() -> buildNotFoundException(CarId));
        car.setImageUrl(imageUrl);
        carRepository.save(car);
        return mapCar(car);
    }

    public CarDTO mapCar(Car car) {
        return new CarDTO(car.getId(), car.getModel(), car.getYear(), car.isDriveable(),
                new EngineDTO(
                        car.getEngine().getId(),
                        car.getEngine().getHorsePower(),
                        car.getEngine().getCapacity()
                ));
    }
}
