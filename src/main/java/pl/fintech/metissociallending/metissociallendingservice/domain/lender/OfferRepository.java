package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Offer save(Offer offer);
    List<Offer> findAllByAuction(Auction auction);
    Optional<Offer> findById(Long id);
    Optional<Offer> findByIdAndLender(Long id, User lender);
    List<Offer> findAllByLender(User lender);
    List<Offer> findAll();
    void delete(Offer offer);
}
