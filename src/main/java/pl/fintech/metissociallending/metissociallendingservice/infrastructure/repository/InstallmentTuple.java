package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Installment;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="INSTALLMENT")
public class InstallmentTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    static InstallmentTuple from(Installment installment) {
        return new InstallmentTuple(installment.getId());
    }
    Installment toDomain(){
        return Installment.builder()
                .id(id)
                .build();
    }
}
