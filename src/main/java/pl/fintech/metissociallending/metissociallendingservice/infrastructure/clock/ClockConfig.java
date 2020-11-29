package pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ClockConfig {

    @Bean
    public pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock.Clock clock(){
        // for the sake of tests
        return new ClockImpl();
        // in production
        //return Clock.system(ZoneId.of("Europe/Warsaw"));
    }
}
