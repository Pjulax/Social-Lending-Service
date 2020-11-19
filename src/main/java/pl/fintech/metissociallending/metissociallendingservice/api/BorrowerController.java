package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDescriptionDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/borrower")
@RequiredArgsConstructor
public class BorrowerController {

   private final BorrowerService borrowerService;

    @GetMapping("/offers")
    public List<Offer> getAllOffersToAuction(@RequestParam Long auction_id){ // change to return Auction DTO
        return borrowerService.getAllOffersToAuction(()->auction_id);
    }

    @PostMapping("/auctions")
    public Auction createNewAuctionSinceNow(@RequestBody AuctionDTO auctionDTO){
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

}
