package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.Builder;
import lombok.Getter;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Installment;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Loan;
import java.util.Date;
import java.util.List;


@Getter
@Builder
public class LoanDTO {
    private final Long id;
    private final String borrower;
    private final String lender;
    private final Double takenAmount;
    private final Double acceptedInterest;
    private final Date startDate;
    private final List<Installment> installments;
    private final Double amountLeft;

    public static LoanDTO fromDomain(Loan loan){
        return LoanDTO.builder()
                .id(loan.getId())
                .borrower(loan.getBorrower().getUsername())
                .lender(loan.getLender().getUsername())
                .takenAmount(loan.getTakenAmount())
                .acceptedInterest(loan.getAcceptedInterest())
                .startDate(loan.getStartDate())
                .installments(loan.getInstallments())
                .amountLeft(loan.getAmountLeft())
                .build();
    }
}
