package pl.fintech.metissociallending.metissociallendingservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;
import io.micrometer.core.instrument.config.validate.ValidationException;
import pl.fintech.metissociallending.metissociallendingservice.api.BorrowerController;
import pl.fintech.metissociallending.metissociallendingservice.api.LenderController;

@ControllerAdvice(basePackageClasses = {LenderController.class, BorrowerController.class})
public class ApiExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException e){
        return new ResponseEntity<>(e.getValidation(), HttpStatus.BAD_REQUEST);
    }
}
