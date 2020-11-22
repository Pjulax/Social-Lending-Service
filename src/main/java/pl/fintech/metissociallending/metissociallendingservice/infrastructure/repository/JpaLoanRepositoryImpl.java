package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Loan;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JpaLoanRepositoryImpl implements LoanRepository {

    private final JpaLoanRepositoryImpl.JpaLoanRepo jpaLoanRepo;

    @Override
    public Loan save(Loan loan) {
        return jpaLoanRepo.save(LoanTuple.from(loan)).toDomain();
    }

    @Override
    public List<Loan> findAllByBorrower(User user) {
        List<LoanTuple> loanTuples = jpaLoanRepo.findAllByBorrower(UserTuple.from(user));
        if(loanTuples.isEmpty())
            return List.of();
        return loanTuples.stream().map(LoanTuple::toDomain).collect(Collectors.toList());
    }

    interface JpaLoanRepo extends JpaRepository<LoanTuple, Long> {
        List<LoanTuple> findAllByBorrower(UserTuple userTuple);
    }
}
