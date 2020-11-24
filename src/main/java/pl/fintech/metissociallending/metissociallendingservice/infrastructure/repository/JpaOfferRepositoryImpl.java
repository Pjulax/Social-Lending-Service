package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JpaOfferRepositoryImpl implements OfferRepository {

    private final JpaOfferRepo jpaOfferRepo;

    @Override
    public Offer save(Offer offer) {
        return jpaOfferRepo.save(OfferTuple.from(offer)).toDomain();
    }

    @Override
    public List<Offer> findAllByAuction(Auction auction) {
        return jpaOfferRepo.findAllByAuction(AuctionTuple.from(auction))
                .stream().map(OfferTuple::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Offer> findById(Long id) {
        Optional<OfferTuple> offerTuple = jpaOfferRepo.findById(id);
        if(offerTuple.isEmpty())
            return Optional.empty();
        return Optional.of(offerTuple.get().toDomain());
    }

    @Override
    public Optional<Offer> findByIdAndLender(Long id, User lender) {
        Optional<OfferTuple> offerTuple = jpaOfferRepo.findByIdAndLender(id, UserTuple.from(lender));
        if(offerTuple.isEmpty())
            return Optional.empty();
        return Optional.of(offerTuple.get().toDomain());
    }

    @Override
    public List<Offer> findAllByLender(User lender) {
        List<OfferTuple> offerTuples = jpaOfferRepo.findAllByLender(UserTuple.from(lender));
        if(offerTuples.isEmpty())
            return List.of();
        return offerTuples.stream().map(OfferTuple::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Offer> findAll() {
        return jpaOfferRepo.findAll()
                .stream().map(OfferTuple::toDomain).collect(Collectors.toList());
    }

    @Override
    public void delete(Offer offer) {
        jpaOfferRepo.delete(OfferTuple.from(offer));
    }

    interface  JpaOfferRepo extends JpaRepository<OfferTuple, Long>{
        Optional<OfferTuple> findByIdAndLender(Long id, UserTuple lender);
        List<OfferTuple> findAllByAuction(AuctionTuple auctionTuple);
        List<OfferTuple> findAllByLender(UserTuple lender);
    }

}
