package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import java.util.Optional;

public interface UserRepository {
    User save(User borrower);
    Optional<User> findById(Long id);
}
