package pl.fintech.metissociallending.metissociallendingservice.infrastructure.security.exceptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                "Access denied", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value
            = { ExistingObjectException.class})
    protected ResponseEntity<Object> handleExistingUserException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "User with that username already exists";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

//    @ExceptionHandler(value
//            = { Here we place all exception.class we want to be handled })
//    protected ResponseEntity<Object> handleSpecificProblem(
//            RuntimeException ex, WebRequest request) {
//        String bodyOfResponse = "This should be application specific";
//        return handleExceptionInternal(ex, bodyOfResponse,
//                new HttpHeaders(), HttpStatus.NEEDED_STATUS_CODE, request);
//    }
}