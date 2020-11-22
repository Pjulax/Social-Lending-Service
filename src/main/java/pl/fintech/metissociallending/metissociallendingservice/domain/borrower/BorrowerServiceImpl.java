package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionWithOffersDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;
import java.time.Clock;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final AuctionRepository auctionRepository;
    private final OfferRepository offerRepository;
    private final UserService userService;
    private final Clock clock;

    @Override
    public Auction createNewAuctionSinceNow(Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand) {
        User borrower = userService.whoami();
        Auction auction = Auction.builder()
                .loanAmount(createNewAuctionSinceNowCommand.getLoanAmount())
                .beginDate(Calendar.getInstance().getTime())
                .endDate( createNewAuctionSinceNowCommand.getEndDate())
                .numberOfInstallments(createNewAuctionSinceNowCommand.getNumberOfInstallments())
                .isClosed(false)
                .description(createNewAuctionSinceNowCommand.getDescription())
                .borrower(borrower)
                .build();
        return auctionRepository.save(auction);
    }

    @Override
    public Auction addAuctionDescription(Command.AddAuctionDescription addAuctionDescription) {
        User borrower = userService.whoami();
        Auction auction = auctionRepository
                .findById(addAuctionDescription.getAuctionId())
                .orElseThrow(() -> new NoSuchElementException("Auction with that id doesn't exist"));
        if(auctionRepository.findByIdAndBorrower(auction.getId(),borrower).isEmpty())
            throw new NoSuchElementException("Auction with that id doesn't exist");
        auction.changeDescription(addAuctionDescription.getDescription());
        return auctionRepository.save(auction);
    }

    @Override
    public List<Auction> getAllAuctions() {
        return auctionRepository.findAllByBorrower(userService.whoami());
    }

    @Override
    public AuctionWithOffersDTO getAuctionById(Long id) {
        Auction auction = auctionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Auction with that id doesn't exist"));
        if(checkIfAuctionIsClosed(auction)) {
            auction.close();
            auctionRepository.save(auction);
        }
        return AuctionWithOffersDTO.fromDomain(auction, offerRepository.findAllByAuction(auction));
    }
    public boolean checkIfAuctionIsClosed(Auction auction){
        return clock.millis()>auction.getEndDate().getTime();
    }
}
