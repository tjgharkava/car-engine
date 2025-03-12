package ge.temo.carengine.cars;

import ge.temo.carengine.CarsService;
import ge.temo.carengine.EngineService;
import ge.temo.carengine.cars.error.NotFoundException;
import ge.temo.carengine.cars.model.CarDTO;
import ge.temo.carengine.cars.model.CarRequest;
import ge.temo.carengine.cars.model.EngineDTO;
import ge.temo.carengine.cars.persistance.Car;
import ge.temo.carengine.cars.persistance.CarRepository;
import ge.temo.carengine.cars.persistance.Engine;
import ge.temo.carengine.cars.user.persistance.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarsServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private EngineService engineService;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private CarsService carsService;

    @Test
    void testGetCars() {
        // Given
        Page<CarDTO> carPage = new PageImpl<>(List.of(new CarDTO(
                1L, "BMW M5", 2003, false, new EngineDTO(
                        1009, 450, 3.2))));
        when(carRepository.findCars(any(PageRequest.class))).thenReturn(carPage);

        // When
        Page<CarDTO> result = carsService.getCars(0, 10);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals("BMW M5", result.getContent().get(0).getModel());
        verify(carRepository).findCars(any(PageRequest.class));
    }

    @Test
    void testAddCar() {
        // Given
        CarRequest carRequest = new CarRequest("BMW", 2022, true, 1L);
        Engine engine = new Engine();
        engine.setId(1009L);
        engine.setHorsePower(450);
        engine.setCapacity(3.2);

        when(engineService.findEngine(1L)).thenReturn(engine);

        // When
        carsService.addCar(carRequest);

        // Then
        verify(carRepository).save(any(Car.class));
    }

    @Test
    void testFindCar_CarExists() {
        // Given
        Engine engine = new Engine();
        engine.setId(1009L);
        engine.setHorsePower(400);
        engine.setCapacity(3.2);

        Car car = new Car();
        car.setId(1L);
        car.setModel("BMW M5");
        car.setYear(2022);
        car.setDriveable(true);
        car.setEngine(engine);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        // When
        CarDTO result = carsService.findCar(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("BMW M5", result.getModel());
        assertEquals(2022, result.getYear());
        assertEquals(true, result.isDriveable());
        assertEquals(1009L, result.getEngine().getId());
        assertEquals(400, result.getEngine().getHorsePower());
        assertEquals(3.2, result.getEngine().getCapacity());
        verify(carRepository).findById(1L);

    }

    @Test
    void testFindCar_CarExist() {
        // Given
        Engine engine = new Engine();
        engine.setId(1009L);
        engine.setHorsePower(400);
        engine.setCapacity(3.2);

        Car car = new Car();
        car.setId(1L);
        car.setModel("BMW M5");
        car.setYear(2022);
        car.setDriveable(true);
        car.setEngine(engine);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        // When
        CarDTO result = carsService.findCar(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("BMW M5", result.getModel());
        assertEquals(2022, result.getYear());
        assertEquals(true, result.isDriveable());
        assertEquals(1009L, result.getEngine().getId());
        assertEquals(400, result.getEngine().getHorsePower());
        assertEquals(3.2, result.getEngine().getCapacity());
        verify(carRepository).findById(1L);

    }

    @Test
    void testFindCar_CarDoesNotExist() {
        // Given
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(NotFoundException.class, () -> carsService.findCar(1L));

    }

    @Test
    void testDeleteCar() {
        // Given
        carsService.deleteCar(1L);

        // Then
        verify(carRepository).deleteById(1L);
    }

    @Test
    void testSetCarImage_CarExists() {
        // Given
        Car car = new Car();
        car.setId(1009L);
        car.setModel("BMW M5");
        car.setYear(2022);
        car.setDriveable(true);
        car.setImageUrl("some_image.jpg");

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        // When
        Car result = carsService.setCarImage(1L, "new_image.jpg");

        // Then
        assertNotNull(result);
        assertEquals("new_image.jpg", result.getImageUrl());
        verify(carRepository).save(car);
    }


}
