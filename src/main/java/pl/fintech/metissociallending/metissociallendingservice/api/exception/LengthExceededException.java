package pl.fintech.metissociallending.metissociallendingservice.api.exception;

public class LengthExceededException extends RuntimeException {
    public LengthExceededException(String message) {
        super(message);
    }

    public LengthExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
