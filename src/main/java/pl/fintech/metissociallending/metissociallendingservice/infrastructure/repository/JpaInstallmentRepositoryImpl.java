package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Installment;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.InstallmentRepository;

@RequiredArgsConstructor
public class JpaInstallmentRepositoryImpl implements InstallmentRepository {
    private final JpaInstallmentRepositoryImpl.JpaInstallmentRepo jpaInstallmentRepo;

    @Override
    public Installment save(Installment installment) {
        return jpaInstallmentRepo.save(InstallmentTuple.from(installment)).toDomain();
    }


    interface JpaInstallmentRepo extends JpaRepository<InstallmentTuple, Long> {
    }
}
