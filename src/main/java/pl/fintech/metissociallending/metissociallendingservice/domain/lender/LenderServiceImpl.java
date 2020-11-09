package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;

import java.util.Calendar;
import java.util.List;


@RequiredArgsConstructor
@Service
public class LenderServiceImpl implements LenderService {
    private final AuctionRepository auctionRepository;
    private final OfferRepository offerRepository;


    @Override
    public Offer submitOffer(Command.SubmitOffer submitOfferCommand) {
        Auction auction = auctionRepository.findById(submitOfferCommand.getOfferId()).orElseThrow();
        if(submitOfferCommand.getProposedAnnualPercentageRate()<.0d)
            throw new IllegalArgumentException("Proposed annual percentage rate cannot be negative ");
        Offer offer = Offer.builder()
                .auction(auction)
                .annualPercentageRate(submitOfferCommand.getProposedAnnualPercentageRate())
                .date(Calendar.getInstance().getTime())
                .build();
        offer = offerRepository.save(offer);
        return offer;
    }

    @Override
    public void cancelOffer(Command.CancelOffer cancelOfferCommand) {
        //TODO
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }
}
