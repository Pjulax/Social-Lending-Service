package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import java.util.List;

public interface OfferRepository {
    Offer save(Offer offer);
    List<Offer> findAll();
}
