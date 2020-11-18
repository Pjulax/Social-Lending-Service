package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;

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
    public List<Offer> findAll() {
        return jpaOfferRepo.findAll()
                .stream().map(OfferTuple::toDomain).collect(Collectors.toList());
    }

    interface  JpaOfferRepo extends JpaRepository<OfferTuple, Long>{
        List<OfferTuple> findAllByAuction(AuctionTuple auctionTuple);
    }

}
