package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class Loan {
    private final Long id;
    private final User borrower;
    private final User lender;
    private final Double takenAmount;
    private final Double acceptedInterest;
    private final Date startDate;
    private final List<Installment> installments;
}
