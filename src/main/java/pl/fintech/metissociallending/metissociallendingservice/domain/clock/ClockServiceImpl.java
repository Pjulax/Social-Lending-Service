package pl.fintech.metissociallending.metissociallendingservice.domain.clock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock.Clock;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class ClockServiceImpl implements ClockService{

    private final Clock clock;

    @Override
    public Date getTime() {
        return new Date(clock.millis());
    }


    @Override
    public void addDays(int days) {
        clock.addDays(days);
    }
}
