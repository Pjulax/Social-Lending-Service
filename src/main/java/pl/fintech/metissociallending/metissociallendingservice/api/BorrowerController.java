package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/borrower")
@RequiredArgsConstructor
public class BorrowerController {

   private final BorrowerService borrowerService;


    @PostMapping("/my-new-auction")
    public Auction createNewAuctionSinceNow(@RequestBody AuctionDTO auctionDTO){
        return borrowerService.createNewAuctionSinceNow(new BorrowerService.Command.CreateNewAuctionSinceNow() {
            @Override
            public BigDecimal getLoanAmount() {
                return BigDecimal.valueOf(auctionDTO.getLoanAmount());
            }

            @Override
            public Date getEndDate() {
                Date date = new Date();
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(auctionDTO.getEndDate());
                }catch (ParseException e){
                    System.out.println(e);
                }
                return date;
            }

            @Override
            public Integer getNumberOfInstallments() {
                return auctionDTO.getNumberOfInstallments();
            }

            @Override
            public Long getUserId() {
                return auctionDTO.getUserId();
            }
        });
    }

    @GetMapping("/all-my-auctions")
    public List<Auction> getAllAuctions(@RequestParam Long borrower_id){ // change to return Auction DTO
        return borrowerService.getAllAuctions(()->borrower_id);
    }
}
