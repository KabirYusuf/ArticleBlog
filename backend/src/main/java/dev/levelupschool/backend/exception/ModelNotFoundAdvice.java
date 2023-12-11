package dev.levelupschool.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ModelNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String modelNotFoundHandler(ModelNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(InterceptorException.class)
    String handleInterceptorExceptionHandler(InterceptorException interceptorException){
        return  interceptorException.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleUserException(UserException userException) {
        return userException.getMessage();
    }
    @ResponseBody
    @ExceptionHandler(ConfigurationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String handleConfigurationException(ConfigurationException configurationException) {
        return configurationException.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleSecurityException(SecurityException securityException) {
        return securityException.getMessage();

    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (message1, message2) -> message1 + ", " + message2));

        errors.put("description", "Validation failed");
        errors.put("errors", fieldErrors);
        return errors;
    }

}
