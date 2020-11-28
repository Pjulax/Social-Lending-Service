package pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock;

import java.time.Instant;

public interface Clock {
    long millis();
    void restart(long starTime, int multiplier);
    void addDays(int days);

    Instant instant();
}
