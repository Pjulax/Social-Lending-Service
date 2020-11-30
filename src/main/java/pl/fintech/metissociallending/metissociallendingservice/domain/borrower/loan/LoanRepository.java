package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findByIdAndBorrower(Long id, User borrower);
    List<Loan> findAllByBorrower(User borrower);
    List<Loan> findAllByLender(User lender);
    List<Loan> findAll();
}
