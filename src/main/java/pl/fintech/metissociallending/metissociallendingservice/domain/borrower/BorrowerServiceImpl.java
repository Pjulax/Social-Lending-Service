package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.api.exception.ExistingObjectException;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final OfferRepository offerRepository;
    private final UserService userService;

    @Override
    public Auction createNewAuctionSinceNow(Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand) {
        User borrower = userService.whoami();
        Auction auction = Auction.builder()
                .loanAmount(createNewAuctionSinceNowCommand.getLoanAmount())
                .beginDate(Calendar.getInstance().getTime())
                .endDate( createNewAuctionSinceNowCommand.getEndDate())
                .numberOfInstallments(createNewAuctionSinceNowCommand.getNumberOfInstallments())
                .description(createNewAuctionSinceNowCommand.getDescription())
                .build();
        auction = auctionRepository.save(auction);
        borrower.addAuction(auction);
        userRepository.save(borrower);
        return auction;
    }

    @Override
    public Auction addAuctionDescription(Command.AddAuctionDescription addAuctionDescription) {
        Auction auction = auctionRepository
                .findById(addAuctionDescription.getAuctionId())
                .orElseThrow(() -> new NoSuchElementException("Auction with that id doesn't exist"));

        auction.changeDescription(addAuctionDescription.getDescription());

        return auctionRepository.save(auction);
    }

    @Override
    public List<Auction> getAllAuctions() {
        return userService.whoami().getAuctions();
    }

    @Override
    public List<Offer> getAllOffersToAuction(BorrowerService.Query.GetAllOffersToAuction getAllOffersToAuctionQuery) {
        Auction auction = auctionRepository.findById(getAllOffersToAuctionQuery.getAuctionId()).orElseThrow();
        return offerRepository.findAllByAuction(auction);
    }
}
