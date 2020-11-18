package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByOffer(Offer offer);
}
