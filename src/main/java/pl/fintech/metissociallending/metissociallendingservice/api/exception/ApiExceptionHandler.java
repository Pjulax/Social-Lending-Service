package pl.fintech.metissociallending.metissociallendingservice.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;
import io.micrometer.core.instrument.config.validate.ValidationException;
import org.springframework.web.context.request.WebRequest;
import pl.fintech.metissociallending.metissociallendingservice.api.BorrowerController;
import pl.fintech.metissociallending.metissociallendingservice.api.LenderController;
import pl.fintech.metissociallending.metissociallendingservice.api.UserController;

@ControllerAdvice(basePackageClasses = {UserController.class, LenderController.class, BorrowerController.class})
public class ApiExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException e){
        return new ResponseEntity<>(e.getValidation(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ExistingObjectException.class)
    public ResponseEntity<Object> handleExistingUserException(ExistingObjectException ex) {
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleExistingUserException(IllegalArgumentException ex) {
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
