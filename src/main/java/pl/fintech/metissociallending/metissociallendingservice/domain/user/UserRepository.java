package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);
}
