package pl.fintech.metissociallending.metissociallendingservice.domain.clock;

import java.time.Instant;
import java.util.Date;

public interface ClockService {
    Date getTime();
    void restart(int multiplier);
    void addDays(int days);
    Instant instant();
}
