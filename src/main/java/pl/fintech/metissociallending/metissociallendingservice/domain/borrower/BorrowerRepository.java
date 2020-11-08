package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import java.util.Optional;

public interface BorrowerRepository {
    Borrower save(Borrower borrower);
    Optional<Borrower> findById(Long id);
}
