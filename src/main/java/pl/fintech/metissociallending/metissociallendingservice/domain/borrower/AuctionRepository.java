package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository {
    Auction save(Auction auction);
    Optional<Auction> findById(Long id);
    List<Auction> findAll();
}
