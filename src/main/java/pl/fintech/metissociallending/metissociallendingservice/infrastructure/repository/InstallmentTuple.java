package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Installment;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.InstallmentStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="INSTALLMENT")
public class InstallmentTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long index;
    @Temporal(TemporalType.TIMESTAMP)
    private Date due;
    private BigDecimal baseAmount;
    private BigDecimal interestAmount;
    private BigDecimal fineAmount;
    private BigDecimal totalAmount;
    private BigDecimal leftAmount;
    @Enumerated(EnumType.STRING)
    private InstallmentStatus status;

    static InstallmentTuple from(Installment installment) {
        return new InstallmentTuple(installment.getId(),
                installment.getIndex(),
                installment.getDue(),
                installment.getAmount(),
                installment.getInterest(),
                installment.getFine(),
                installment.getTotal(),
                installment.getLeft(),
                installment.getStatus());
    }

    Installment toDomain(){
        return Installment.builder()
                .id(id)
                .index(index)
                .due(due)
                .amount(baseAmount)
                .interest(interestAmount)
                .fine(fineAmount)
                .total(totalAmount)
                .left(leftAmount)
                .status(status)
                .build();
    }
}