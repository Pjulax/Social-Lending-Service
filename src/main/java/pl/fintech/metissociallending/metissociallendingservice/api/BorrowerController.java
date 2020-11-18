package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionWithOffersDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Loan;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import java.util.List;

@RestController
@RequestMapping("/api/borrower")
@RequiredArgsConstructor
public class BorrowerController {

   private final BorrowerService borrowerService;
   private final LoanService loanService;

    @PostMapping("/auctions")
    public Auction createNewAuctionSinceNow(@RequestBody AuctionDTO auctionDTO){
        return borrowerService.createNewAuctionSinceNow(auctionDTO);
    }

    @GetMapping("/auctions")
    public List<Auction> getAllAuctions(){ // change to return Auction DTO
        return borrowerService.getAllAuctions();
    }

    @GetMapping("/auctions/{id}")
    public AuctionWithOffersDTO getAuctionWithOffers(@PathVariable Long id){
        return borrowerService.getAuctionById(id);
    }

    @PostMapping("/auctions/{auction_id}/accept-offer")
    public LoanDTO acceptOffer(@PathVariable Long auction_id, @RequestParam Long offer_id){

        Loan loan = loanService.acceptOffer(new LoanService.Command.AcceptOffer() {
            @Override
            public Long getAuctionId() {
                return auction_id;
            }

            @Override
            public Long getOfferId() {
                return offer_id;
            }
        });
        return LoanDTO.builder()
                .acceptedInterest(loan.getAcceptedInterest())
                .borrower(loan.getBorrower().getUsername())
                .lender(loan.getLender().getUsername())
                .id(loan.getId())
                .installments(loan.getInstallments())
                .startDate(loan.getStartDate())
                .takenAmount(loan.getTakenAmount())
                .build();
    }
    @GetMapping("/loans")
    public List<Loan> getMyLoans(){
        return loanService.getLoansByBorrower();
    }
}
