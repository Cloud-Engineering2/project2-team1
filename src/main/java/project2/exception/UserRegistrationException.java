package project2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message) {
        super(message);
    }

}