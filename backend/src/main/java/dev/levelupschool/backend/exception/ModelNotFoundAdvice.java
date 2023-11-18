package dev.levelupschool.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleUserException(UserException userException) {
        return userException.getMessage();
    }
    @ResponseBody
    @ExceptionHandler(ConfigurationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String handleConfigurationException(ConfigurationException configurationException) {
        return configurationException.getMessage();
    }
}
