package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Offer save(Offer offer);
    List<Offer> findAllByAuction(Auction auction);
    Optional<Offer> findById(Long id);
    List<Offer> findAll();
}
