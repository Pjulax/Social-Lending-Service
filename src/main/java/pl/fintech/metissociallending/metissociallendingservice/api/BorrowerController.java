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
import pl.fintech.metissociallending.metissociallendingservice.api.exception.ExceptionResponse;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/borrower")
@RequiredArgsConstructor
public class BorrowerController {

    private static final Logger log = LoggerFactory.getLogger(BorrowerController.class);
    private final BorrowerService borrowerService;

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
    public AuctionWithOffersDTO getAuction(@PathVariable Long id){
        return borrowerService.getAuctionById(id);
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
