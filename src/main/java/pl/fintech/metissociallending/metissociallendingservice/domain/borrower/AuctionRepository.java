package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository {
    Auction save(Auction auction);
    Optional<Auction> findById(Long id);
}
