package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
@AllArgsConstructor
public class Auction{
    private final Long id;
    @JsonIgnore
    private User borrower;
    private final BigDecimal loanAmount;
    private final Date beginDate;
    private final Date endDate;
    private final Integer numberOfInstallments;
    private Boolean isClosed;
    private String description;

    public void close(){
        isClosed = false;
    }
    public void open(){
        isClosed = true;
    }

    public void changeDescription(String description) {
         if (!StringUtils.isEmpty(this.description)) {
          throw new IllegalArgumentException("Description in auction already exists");
         }
         this.description = description;
    }
}
