package pl.fintech.metissociallending.metissociallendingservice.api.exception;

public class DateTimeIncorrectFormatException extends RuntimeException{
    public DateTimeIncorrectFormatException(String message) {
        super(message);
    }

    public DateTimeIncorrectFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
