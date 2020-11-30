package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

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
    public Optional<Auction> findByIdAndBorrower(Long id, User borrower) {
        Optional<AuctionTuple> auctionTupleOptional = jpaAuctionRepo.findByIdAndBorrower(id, UserTuple.from(borrower));
        if(auctionTupleOptional.isEmpty())
            return Optional.empty();
        return Optional.of(jpaAuctionRepo.findByIdAndBorrower(id, UserTuple.from(borrower)).get().toDomain());
    }

    @Override
    public List<Auction> findAllByBorrower(User borrower) {
        List<AuctionTuple> auctionTuples = jpaAuctionRepo.findAllByBorrower(UserTuple.from(borrower));
        if(auctionTuples.isEmpty())
            return List.of();
        return auctionTuples.stream().map(AuctionTuple::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Auction> findAll() {
        return jpaAuctionRepo.findAll().stream().map(AuctionTuple::toDomain).collect(Collectors.toList());
    }

    interface JpaAuctionRepo extends JpaRepository<AuctionTuple, Long>{
        Optional<AuctionTuple> findByIdAndBorrower(Long aLong, UserTuple borrower);
        List<AuctionTuple> findAllByBorrower(UserTuple borrower);
    }
}
