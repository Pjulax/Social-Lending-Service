package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Installment {
    private final Long id;
    private final Long index;
    private final Date due;
    private final BigDecimal amount;
    private final BigDecimal interest;
    private final BigDecimal fine;
    private final BigDecimal total;
    private final BigDecimal left;
    private final InstallmentStatus status;
}
