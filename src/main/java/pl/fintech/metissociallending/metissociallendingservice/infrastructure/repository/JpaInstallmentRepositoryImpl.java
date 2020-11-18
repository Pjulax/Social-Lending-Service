package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.InstallmentRepository;

@RequiredArgsConstructor
public class JpaInstallmentRepositoryImpl implements InstallmentRepository {
    private final JpaInstallmentRepositoryImpl.JpaInstallmentsRepo jpaInstallmentsRepo;


    interface JpaInstallmentsRepo extends JpaRepository<LoanTuple, Long> {
    }
}
