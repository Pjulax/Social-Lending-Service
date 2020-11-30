package pl.fintech.metissociallending.metissociallendingservice.api;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.clock.ClockService;
import java.util.Date;

/**
 * ONLY FOR TESTING AND DEMONSTRATION PURPOSES
 * Clock controller that implements own
 * implementation of clock that can be speed up
 * or slow down. Administrator can also add days
 * in order to check changing state of installments
 * and loans
 * <p><ul>
 *  <li>add days .</li>
 *  <li>get current timer</li>
 * </ul></p>
 * @see ClockService
 */
@RestController
@Api(tags = "Clock")
@RequestMapping("/api/clock")
@RequiredArgsConstructor
public class ClockController {

    private final ClockService clockService;

    @GetMapping("/time")
    public Date getTime(){
        return clockService.getTime();
    }

    @PostMapping("/add-days")
    public void addDays(@RequestParam int days){
        clockService.addDays(days);
    }
}
