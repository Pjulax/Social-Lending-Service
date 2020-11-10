package pl.fintech.metissociallending.metissociallendingservice.infrastructure.security.exceptions;

public class ExistingObjectException extends RuntimeException{
    public ExistingObjectException() {
        super("This entity already exists in database");
    }
}
