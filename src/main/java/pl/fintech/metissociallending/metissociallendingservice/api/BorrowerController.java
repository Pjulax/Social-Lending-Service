package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import java.util.List;

@RestController
@RequestMapping("/api/borrower")
@RequiredArgsConstructor
public class BorrowerController {

   private final BorrowerService borrowerService;


    @PostMapping("/my-new-auction")
    public Auction createNewAuctionSinceNow(@RequestBody AuctionDTO auctionDTO){
        return borrowerService.createNewAuctionSinceNow(auctionDTO);
    }

    @GetMapping("/all-my-auctions")
    public List<Auction> getAllAuctions(@RequestParam Long borrower_id){ // change to return Auction DTO
        return borrowerService.getAllAuctions(()->borrower_id);
    }
}
