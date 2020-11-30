package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<UserTuple> borrowerTupleOptional = jpaUserRepo.findByUsername(username);
        if(borrowerTupleOptional.isEmpty())
            return Optional.empty();
        return Optional.of(borrowerTupleOptional.get().toDomain());
    }

    @Override
    public List<User> findAll() {
        List<UserTuple> userTupleList = jpaUserRepo.findAll();
        if(userTupleList.isEmpty())
            return List.of();
        return userTupleList.stream().map(UserTuple::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteByUsername(String username) {
        jpaUserRepo.deleteByUsername(username);
    }

    public interface JpaUserRepo extends JpaRepository<UserTuple, Long>{
        Optional<UserTuple> findByUsername(String username);
        @Transactional
        void deleteByUsername(String username);
    }
}
