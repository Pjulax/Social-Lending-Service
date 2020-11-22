package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.util.List;

public interface LoanRepository {
    Loan save(Loan loan);

    List<Loan> findAllByBorrower(User borrower);
}
