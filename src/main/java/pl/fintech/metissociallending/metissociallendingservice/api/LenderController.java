package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    public void submitOffer(@RequestBody OfferDTO offerDTO){
        try{
            lenderService.submitOffer(new LenderService.Command.SubmitOffer() {
                @Override
                public Long getOfferId() {
                    return offerDTO.getAuctionId();
                }

                @Override
                public Double getProposedAnnualPercentageRate() {
                    return offerDTO.getAnnualPercentageRate();
                }
            });
        } catch (NoSuchElementException e){
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provided account id wasn't found");
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/my-offers")
    public List<Offer> getAllOffers(){
        return lenderService.getAllOffers();
    }

}
