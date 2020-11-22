package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;


import io.micrometer.core.instrument.util.StringUtils;
import lombok.*;
import pl.fintech.metissociallending.metissociallendingservice.api.exception.ExistingObjectException;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
@AllArgsConstructor
public class Auction{
    private final Long id;
    private final BigDecimal loanAmount;
    private final Date beginDate;
    private final Date endDate;
    private final Integer numberOfInstallments;
    private final Boolean isClosed;
    private String description;

    public void changeDescription(String description) {
         if (!StringUtils.isEmpty(this.description)) {
          throw new ExistingObjectException("Description in auction already exists");
         }
         this.description = description;
    }
}
