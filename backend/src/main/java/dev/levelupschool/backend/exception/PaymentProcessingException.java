package dev.levelupschool.backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Setter
@Getter
public class PaymentProcessingException extends RuntimeException {
    private final HttpStatus status;

    public PaymentProcessingException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}

