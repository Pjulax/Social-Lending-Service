package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import java.util.List;
import java.util.Optional;

public interface AuctionRepository {
    Auction save(Auction auction);
    Optional<Auction> findById(Long id);
    Optional<Auction> findByIdAndBorrower(Long id, User borrower);
    List<Auction> findAllByBorrower(User borrower);
    List<Auction> findAll();
}
