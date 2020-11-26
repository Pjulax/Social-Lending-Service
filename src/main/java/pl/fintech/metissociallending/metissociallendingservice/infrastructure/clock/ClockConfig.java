package pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    @Bean
    public pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock.Clock clock(){
        // for a sake of tests
        return new ClockImpl(1, Clock.system(ZoneId.of("Europe/Warsaw")).millis());
        // in production
        //return Clock.system(ZoneId.of("Europe/Warsaw"));
    }
}
