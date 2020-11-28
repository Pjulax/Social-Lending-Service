package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import io.micrometer.core.instrument.config.validate.InvalidReason;
import io.micrometer.core.instrument.config.validate.Validated;
import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock.Clock;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LenderServiceImpl implements LenderService {
    private final AuctionRepository auctionRepository;
    private final OfferRepository offerRepository;
    private final Clock clock;
    private final UserService userService;

    @Override
    public Offer submitOffer(Command.SubmitOffer submitOfferCommand) {
        Auction auction = auctionRepository.findById(submitOfferCommand.getAuctionId()).orElseThrow();
        User user = userService.whoami();
        if(auctionRepository.findByIdAndBorrower(auction.getId(), user).isPresent())
            throw new IllegalArgumentException("User cannot create offer for his own auction");
        if(submitOfferCommand.getProposedAnnualPercentageRate()<=.0d)
            throw new ValidationException(Validated.invalid("annual proposed percentage rate", submitOfferCommand.getProposedAnnualPercentageRate(), " cannot be below or equal 0", InvalidReason.MALFORMED));
        Offer offer = Offer.builder()
                .auction(auction)
                .annualPercentageRate(submitOfferCommand.getProposedAnnualPercentageRate())
                .date(new Date(clock.millis()))
                .lender(user)
                .build();
        return offerRepository.save(offer);
    }

    @Override
    public void cancelOffer(Command.CancelOffer cancelOfferCommand) {
        Offer offer = offerRepository.findById(cancelOfferCommand.getOfferId()).orElseThrow(() -> new NoSuchElementException("Offer with that id doesn't exist"));
        if(offerRepository.findByIdAndLender(cancelOfferCommand.getOfferId(), userService.whoami()).isEmpty())
            throw new IllegalArgumentException("Offer doesn't belong to logged user");
        offerRepository.delete(offer);
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAllByLender(userService.whoami());
    }

    @Override
    public List<Auction> getAllAvailableAuctions() {
        List<Auction> auctions = auctionRepository.findAll();
        auctions.removeAll(auctionRepository.findAllByBorrower(userService.whoami()));
        return auctions.stream().filter(a-> !a.getIsClosed()).collect(Collectors.toList());
    }

}
