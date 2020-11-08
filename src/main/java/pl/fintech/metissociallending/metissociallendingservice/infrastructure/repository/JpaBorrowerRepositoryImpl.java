package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Borrower;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class JpaBorrowerRepositoryImpl implements BorrowerRepository {
    private final JpaBorrowerRepo jpaBorrowerRepo;

    @Override
    public Borrower save(Borrower borrower) {
        return jpaBorrowerRepo.save(BorrowerTuple.from(borrower)).toDomain();
    }

    @Override
    public Optional<Borrower> findById(Long id) {
        Optional<BorrowerTuple> borrowerTupleOptional = jpaBorrowerRepo.findById(id);
        if(borrowerTupleOptional.isEmpty())
            return Optional.empty();
        return Optional.of(borrowerTupleOptional.get().toDomain());
    }

    interface JpaBorrowerRepo extends JpaRepository<BorrowerTuple, Long>{
    }
}
