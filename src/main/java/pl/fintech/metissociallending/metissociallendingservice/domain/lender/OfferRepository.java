package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;

import java.util.List;

public interface OfferRepository {
    Offer save(Offer offer);
    List<Offer> findAllByAuction(Auction auction);
    List<Offer> findAll();
}
