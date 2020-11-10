package pl.fintech.metissociallending.metissociallendingservice.api;

import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.OfferDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.LenderService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/lender")
@RequiredArgsConstructor
public class LenderController {
    private final LenderService lenderService;

    @PostMapping("/submit-offer")
    public Offer submitOffer(@RequestBody OfferDTO offerDTO){
        return lenderService.submitOffer(offerDTO);
    }
    @GetMapping("/my-offers")
    public List<Offer> getAllOffers(){
        return lenderService.getAllOffers();
    }

}
