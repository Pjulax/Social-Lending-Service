package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Installment;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

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
}
