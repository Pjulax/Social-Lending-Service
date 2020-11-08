package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class JpaUserRepositoryImpl implements UserRepository {
    private final JpaUserRepo jpaUserRepo;

    @Override
    public User save(User user) {
        return jpaUserRepo.save(UserTuple.from(user)).toDomain();
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserTuple> borrowerTupleOptional = jpaUserRepo.findById(id);
        if(borrowerTupleOptional.isEmpty())
            return Optional.empty();
        return Optional.of(borrowerTupleOptional.get().toDomain());
    }

    interface JpaUserRepo extends JpaRepository<UserTuple, Long>{
    }
}
