package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;

@RequiredArgsConstructor
public class JpaAuctionRepositoryImpl implements AuctionRepository {
    private final JpaAuctionRepo jpaAuctionRepo;

    @Override
    public Auction save(Auction auction) {
        return jpaAuctionRepo.save(AuctionTuple.from(auction)).toDomain();
    }
    interface JpaAuctionRepo extends JpaRepository<AuctionTuple, Long>{
    }
}
