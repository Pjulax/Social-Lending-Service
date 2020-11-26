package pl.fintech.metissociallending.metissociallendingservice.infrastructure.security;

public interface AES {
    String encrypt(String message);
    String decrypt(String message);
}
