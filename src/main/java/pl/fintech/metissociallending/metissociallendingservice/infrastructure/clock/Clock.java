package pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock;

public interface Clock {
    long millis();
    void addDays(int days);
}
