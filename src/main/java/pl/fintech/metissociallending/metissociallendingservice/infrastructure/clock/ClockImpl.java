package pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock;

import java.time.Instant;
import java.time.ZoneId;

/**
 * This clock can be speed up or days can be added.
 * It is for a reason of tests and demonstration.
 */
public class ClockImpl implements Clock {
    private final java.time.Clock internalClock = java.time.Clock.system(ZoneId.of("Europe/Warsaw"));
    private int multiplier;
    private long startTime;
    private long days = 0;

    public ClockImpl(int multiplier, long startTime){
        this.multiplier = multiplier;
        this.startTime = startTime;
    }
    @Override
    public long millis() {
        final long dayLongs = 24 * 60 * 60 * 1000;
        return days* dayLongs +internalClock.millis()+(internalClock.millis()-startTime)*multiplier;
    }

    @Override
    public void restart(long startTime, int multiplier) {
        this.startTime = startTime;
        this.multiplier = multiplier;
    }

    @Override
    public void addDays(int days) {
        this.days+=days;
    }

    @Override
    public Instant instant() {
        return internalClock.instant();
    }


}
