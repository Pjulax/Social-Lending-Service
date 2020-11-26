package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import lombok.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Loan {
    private final Long id;
    private final User borrower;
    private final User lender;
    private final Double takenAmount;
    private final Double acceptedInterest;
    private final Date startDate;
    private final List<Installment> installments;
    private Double amountLeft;

    public void calculateLeft(){
        Double sumLeft = 0d;
        for (Installment in: installments) {
            if(!in.getStatus().equals(InstallmentStatus.PAID))
                sumLeft += in.getTotal().setScale(2, RoundingMode.FLOOR).doubleValue();
        }
        amountLeft = BigDecimal.valueOf(sumLeft).setScale(2,RoundingMode.HALF_UP).doubleValue();
    }

}
