package pl.fintech.metissociallending.metissociallendingservice.api.exception;

public class ExistingObjectException extends RuntimeException {
    public ExistingObjectException(String message) {
        super(message);
    }

    public ExistingObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
