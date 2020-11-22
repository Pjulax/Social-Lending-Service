package pl.fintech.metissociallending.metissociallendingservice.api.exception;

public class ExistingObjectException extends RuntimeException {
    public ExistingObjectException(String message) {
        super(message);
    }
}
