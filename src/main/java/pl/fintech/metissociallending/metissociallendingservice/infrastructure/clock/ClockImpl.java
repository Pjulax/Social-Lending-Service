package pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock;

import lombok.NoArgsConstructor;

import java.time.ZoneId;

/**
 * This clock can be speed up or days can be added.
 * It is for a reason of tests and demonstration.
 */
@NoArgsConstructor
public class ClockImpl implements Clock {
    private final java.time.Clock internalClock = java.time.Clock.system(ZoneId.of("Europe/Warsaw"));

    private long days = 0;

    @Override
    public long millis() {
        final long dayLongs = 24 * 60 * 60 * 1000L;
        return days* dayLongs +internalClock.millis();
    }


    @Override
    public void addDays(int days) {
        this.days+=days;
    }
}
