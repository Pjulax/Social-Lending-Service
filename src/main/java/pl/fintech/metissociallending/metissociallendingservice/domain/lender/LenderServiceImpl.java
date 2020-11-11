package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import io.micrometer.core.instrument.config.validate.InvalidReason;
import io.micrometer.core.instrument.config.validate.Validated;
import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;

import java.time.Clock;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Service
public class LenderServiceImpl implements LenderService {
    private final AuctionRepository auctionRepository;
    private final OfferRepository offerRepository;
    private final Clock clock;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Offer submitOffer(Command.SubmitOffer submitOfferCommand) {
        Auction auction = auctionRepository.findById(submitOfferCommand.getAuctionId()).orElseThrow();
        User user = userService.whoami();
        if(submitOfferCommand.getProposedAnnualPercentageRate()<.0d)
            throw new ValidationException(Validated.invalid("annual proposed percentage rate", submitOfferCommand.getProposedAnnualPercentageRate(), " cannot be below or equal 0", InvalidReason.MALFORMED));
        Offer offer = Offer.builder()
                .auction(auction)
                .annualPercentageRate(submitOfferCommand.getProposedAnnualPercentageRate())
                .date(new Date(clock.millis()))
                .build();
        offer = offerRepository.save(offer);
        user.addOffer(offer);
        userRepository.save(user);
        return offer;
    }

    @Override
    public void cancelOffer(Command.CancelOffer cancelOfferCommand) {
        //TODO
    }

    @Override
    public List<Offer> getAllOffers() {
        return userService.whoami().getOffers();
    }

    @Override
    public List<Auction> getAllAvailableAuctions() {
        return auctionRepository.findAll();
    }
}
