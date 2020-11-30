package pl.fintech.metissociallending.metissociallendingservice.api.exception;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

import io.micrometer.core.instrument.config.validate.ValidationException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.fintech.metissociallending.metissociallendingservice.api.BorrowerController;
import pl.fintech.metissociallending.metissociallendingservice.api.LenderController;
import pl.fintech.metissociallending.metissociallendingservice.api.UserController;

@ControllerAdvice(basePackageClasses = {UserController.class, LenderController.class, BorrowerController.class})
public class GeneralApiExceptionHandler
        extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GeneralApiExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchElementException(NoSuchElementException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex){
        return new ResponseEntity<>(ex.getValidation(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FeignException.FeignClientException.class)
    public ResponseEntity<ExceptionResponse> handleFeignClientException(FeignException.FeignClientException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FeignException.FeignServerException.class)
    public ResponseEntity<ExceptionResponse> handleFeignServerException(FeignException.FeignServerException ex, WebRequest req){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler({ RuntimeException.class,
                        Exception.class})
    public ResponseEntity<ExceptionResponse> handleCaughtNotKnownExceptions(Exception ex, WebRequest req) {
        log.error("Unexpected error!", ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}