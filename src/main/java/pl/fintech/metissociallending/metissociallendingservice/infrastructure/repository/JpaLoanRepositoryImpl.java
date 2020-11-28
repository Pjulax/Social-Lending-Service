package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Loan;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JpaLoanRepositoryImpl implements LoanRepository {

    private final JpaLoanRepositoryImpl.JpaLoanRepo jpaLoanRepo;

    @Override
    public Loan save(Loan loan) {
        return jpaLoanRepo.save(LoanTuple.from(loan)).toDomain();
    }

    @Override
    public Optional<Loan> findByIdAndBorrower(Long id, User borrower) {
        Optional<LoanTuple> optionalLoanTuple = jpaLoanRepo.findByIdAndBorrower(id, UserTuple.from(borrower));
        if(optionalLoanTuple.isEmpty())
            return Optional.empty();
        return Optional.of(optionalLoanTuple.get().toDomain());
    }

    @Override
    public List<Loan> findAllByBorrower(User borrower) {
        List<LoanTuple> loanTuples = jpaLoanRepo.findAllByBorrower(UserTuple.from(borrower));
        if(loanTuples.isEmpty())
            return List.of();
        return loanTuples.stream().map(LoanTuple::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findAllByLender(User lender) {
        List<LoanTuple> loanTuples = jpaLoanRepo.findAllByLender(UserTuple.from(lender));
        if(loanTuples.isEmpty())
            return List.of();
        return loanTuples.stream().map(LoanTuple::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findAll() {
        List<LoanTuple> loanTuples = jpaLoanRepo.findAll();
        if(loanTuples.isEmpty())
            return List.of();
        return loanTuples.stream().map(LoanTuple::toDomain).collect(Collectors.toList());
    }

    interface JpaLoanRepo extends JpaRepository<LoanTuple, Long> {
        List<LoanTuple> findAllByBorrower(UserTuple userTuple);
        List<LoanTuple> findAllByLender(UserTuple userTuple);
        Optional<LoanTuple> findByIdAndBorrower(Long id, UserTuple userTuple);
    }
}
