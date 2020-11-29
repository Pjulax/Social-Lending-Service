package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.SubmitOfferDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.LenderService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import java.util.List;

@RestController
@RequestMapping("/api/lender")
@RequiredArgsConstructor
public class LenderController {
    private final LenderService lenderService;
    private final LoanService loanService;

    @PostMapping("/offers")
    public Offer submitOffer(@RequestBody SubmitOfferDTO submitOfferDTO){
        return lenderService.submitOffer(submitOfferDTO);
    }
    @GetMapping("/offers")
    public List<Offer> getAllOffers(){
        return lenderService.getAllOffers();
    }

    @DeleteMapping("/offers/{offer_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOffer(@PathVariable Long offer_id){
        lenderService.cancelOffer(()->offer_id);
    }

    @GetMapping("/auctions")
    public List<Auction> getAllAvailableAuctions(){
        return lenderService.getAllAvailableAuctions();
    }

    @GetMapping("/investments")
    public List<LoanDTO> getInvestments(){
        return loanService.getAllInvestments();
    }

}
