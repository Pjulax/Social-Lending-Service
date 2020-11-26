package pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock;

import java.time.ZoneId;

public class ClockImpl implements Clock {
    private final java.time.Clock internalClock = java.time.Clock.system(ZoneId.of("Europe/Warsaw"));
    private int multiplier;
    private long startTime;
    public ClockImpl(int multiplier, long startTime){
        this.multiplier = multiplier;
        this.startTime = startTime;
    }
    @Override
    public long millis() {
        return internalClock.millis()+(internalClock.millis()-startTime)*multiplier;
    }

    @Override
    public void restart(long startTime, int multiplier) {
        this.startTime = startTime;
        this.multiplier = multiplier;
    }
}
