package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JpaAuctionRepositoryImpl implements AuctionRepository {
    private final JpaAuctionRepo jpaAuctionRepo;

    @Override
    public Auction save(Auction auction) {
        return jpaAuctionRepo.save(AuctionTuple.from(auction)).toDomain();
    }

    @Override
    public Optional<Auction> findById(Long id) {
        Optional<AuctionTuple> auctionTupleOptional = jpaAuctionRepo.findById(id);
        if(auctionTupleOptional.isEmpty())
            return Optional.empty();
        Auction auction = auctionTupleOptional.get().toDomain();
        return Optional.of(auction);
    }

    @Override
    public List<Auction> findAll() {
        return jpaAuctionRepo.findAll().stream().map(AuctionTuple::toDomain).collect(Collectors.toList());
    }


    interface JpaAuctionRepo extends JpaRepository<AuctionTuple, Long>{
    }
}
