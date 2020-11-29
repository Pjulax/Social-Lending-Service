package pl.fintech.metissociallending.metissociallendingservice.api.exception;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import pl.fintech.metissociallending.metissociallendingservice.api.BorrowerController;
import pl.fintech.metissociallending.metissociallendingservice.api.LenderController;
import pl.fintech.metissociallending.metissociallendingservice.api.UserController;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice(basePackageClasses = {UserController.class, LenderController.class, BorrowerController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpecificApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest req) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        StringBuilder errorsStrBuilder = new StringBuilder();
        errors.forEach(errorsStrBuilder::append);
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), errorsStrBuilder.toString(), req.getDescription(false));
        return  new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
