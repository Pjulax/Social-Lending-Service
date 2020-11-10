package pl.fintech.metissociallending.metissociallendingservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.xml.bind.ValidationException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e){
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<Object> handleValidationException(ValidationException e){
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }
}
