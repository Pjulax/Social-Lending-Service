package pl.fintech.metissociallending.metissociallendingservice.api;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDescriptionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionWithOffersDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanService;
import javax.validation.Valid;
import java.util.List;

/**
 * Allows borrower to
 * <p><ul>
 *  <li>create auction.</li>
 *  <li>see auctions.</li>
 *  <li>see offers applied for auctions.</li>
 *  <li>see taken loans.</li>
 *  <li>pay next installment of given loan.</li>
 * </ul></p>
 * @see BorrowerService
 * @see LoanService
 */
@Validated
@RestController
@Api(tags = "Borrower")
@RequestMapping("/api/borrower")
@RequiredArgsConstructor
public class BorrowerController {

   private final BorrowerService borrowerService;
   private final LoanService loanService;

    @PostMapping("/auctions")
    public Auction createNewAuctionSinceNow( @Valid @RequestBody AuctionDTO auctionDTO){
        return borrowerService.createNewAuctionSinceNow(auctionDTO);
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String hello(){
        return "Welcome!";
    }
    @PutMapping("/auctions")
    public Auction addAuctionDescription( @Valid @RequestBody AuctionDescriptionDTO auctionDescriptionDTO){
        return borrowerService.addAuctionDescription(auctionDescriptionDTO);
    }

    @GetMapping("/auctions")
    public List<Auction> getAllAuctions(){
        return borrowerService.getAllAuctions();
    }

    @GetMapping("/auctions/{id}")
    public AuctionWithOffersDTO getAuctionWithOffers(@PathVariable Long id){
        return borrowerService.getAuctionById(id);
    }

    @PostMapping("/auctions/{auction_id}/accept-offer")
    public LoanDTO acceptOffer(@PathVariable Long auction_id, @RequestParam Long offer_id){

        return loanService.acceptOffer(new LoanService.Command.AcceptOffer() {
            @Override
            public Long getAuctionId() {
                return auction_id;
            }
            @Override
            public Long getOfferId() {
                return offer_id;
            }
        });

    }
    @GetMapping("/loans")
    public List<LoanDTO> getMyLoans(){
        return loanService.getLoansByBorrower();
    }

    @PostMapping("/loans/{loan_id}/pay-next-installment")
    public void payNextInstallment(@PathVariable Long loan_id, @RequestParam Double amount){
        loanService.payNextInstallment(new LoanService.Command.PayNextInstallment() {
            @Override
            public Long getLoanId() {
                return loan_id;
            }
            @Override
            public Double getAmount() {
                return amount;
            }
        });
    }

}
