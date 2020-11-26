package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.clock.ClockService;

import java.util.Date;

@RestController
@RequestMapping("/api/clock")
@RequiredArgsConstructor
public class ClockController {

    private final ClockService clockService;

    @GetMapping("/time")
    public Date getTime(){
        return clockService.getTime();
    }

    @PostMapping("/restart")
    public void restart(@RequestParam int multiplier){
        clockService.restart(multiplier);
    }

    @PostMapping("/add-days")
    public void addDays(@RequestParam int days){
        clockService.addDays(days);
    }
}
