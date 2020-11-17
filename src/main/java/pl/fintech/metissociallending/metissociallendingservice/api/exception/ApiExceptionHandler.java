package pl.fintech.metissociallending.metissociallendingservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.NoSuchElementException;
import io.micrometer.core.instrument.config.validate.ValidationException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.fintech.metissociallending.metissociallendingservice.api.BorrowerController;
import pl.fintech.metissociallending.metissociallendingservice.api.LenderController;
import pl.fintech.metissociallending.metissociallendingservice.api.UserController;

@ControllerAdvice(basePackageClasses = {UserController.class, LenderController.class, BorrowerController.class})
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchElementException(NoSuchElementException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
    //todo - change handleValidationException to exceptionResponse handler
//    Not fancy, but working version
//    @ExceptionHandler(ValidationException.class)
//    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException ex, WebRequest req){
//        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getValidation().toString(), req.getDescription(false));
//        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
//    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex){
        return new ResponseEntity<>(ex.getValidation(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ExistingObjectException.class)
    public ResponseEntity<ExceptionResponse> handleExistingUserException(ExistingObjectException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleExistingUserException(IllegalArgumentException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(LengthExceededException.class)
    public ResponseEntity<ExceptionResponse> handleLengthExceededException(LengthExceededException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
