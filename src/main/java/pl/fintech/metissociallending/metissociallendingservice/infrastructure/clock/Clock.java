package pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock;

public interface Clock {
    long millis();
    void restart(long starTime, int multiplier);
}
