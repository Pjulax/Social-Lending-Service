package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDescriptionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionWithOffersDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Loan;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanService;
import pl.fintech.metissociallending.metissociallendingservice.api.exception.ExceptionResponse;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/borrower")
@RequiredArgsConstructor
public class BorrowerController {

   private final BorrowerService borrowerService;
   private final LoanService loanService;
   private static final Logger log = LoggerFactory.getLogger(BorrowerController.class);

    @PostMapping("/auctions")
    public Auction createNewAuctionSinceNow( @Valid @RequestBody AuctionDTO auctionDTO){
        return borrowerService.createNewAuctionSinceNow(auctionDTO);
    }

    @PutMapping("/auctions")
    public Auction addAuctionDescription( @Valid @RequestBody AuctionDescriptionDTO auctionDescriptionDTO){
        return borrowerService.addAuctionDescription(auctionDescriptionDTO);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest req) {
        log.error("Unexpected error!", ex);
        // Temporary solution
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        StringBuilder errorsStrBuilder = new StringBuilder();
        errors.forEach(errorsStrBuilder::append);
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), errorsStrBuilder.toString(), req.getDescription(false));
        return  new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
