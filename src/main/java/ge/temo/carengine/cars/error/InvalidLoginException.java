package ge.temo.carengine.cars.error;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super(message);
    }
}
