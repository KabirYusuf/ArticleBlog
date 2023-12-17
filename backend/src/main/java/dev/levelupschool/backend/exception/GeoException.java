package dev.levelupschool.backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Setter
@Getter
public class GeoException extends RuntimeException{
    private final HttpStatus status;
    public GeoException(String message,  HttpStatus status){
        super(message);
        this.status = status;
    }
}
