package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Loan;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="LOAN")
public class LoanTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private UserTuple borrower;
    @ManyToOne
    private UserTuple lender;
    private Double takenAmount;
    private Double acceptedInterest;
    private Date startDate;
    @OneToMany
    private List<InstallmentTuple> installments;

    static LoanTuple from(Loan loan){
        return new LoanTuple(loan.getId(),
                UserTuple.from(loan.getBorrower()),
                UserTuple.from(loan.getLender()),
                loan.getTakenAmount(),
                loan.getAcceptedInterest(),
                loan.getStartDate(),
                loan.getInstallments()==null?List.of():loan.getInstallments().stream().map(InstallmentTuple::from).collect(Collectors.toList()));
    }
    Loan toDomain() {
        return Loan.builder()
                .id(id)
                .borrower(borrower.toDomain())
                .lender(lender.toDomain())
                .takenAmount(takenAmount)
                .acceptedInterest(acceptedInterest)
                .startDate(startDate)
                .installments(installments==null?List.of():installments.stream().map(InstallmentTuple::toDomain).collect(Collectors.toList()))
                .build();
    }
}
