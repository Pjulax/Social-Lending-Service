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
<<<<<<< HEAD
    private final Boolean isClosed;
=======
    private String description;

    public void changeDescription(String description) {
         if (!StringUtils.isEmpty(this.description)) {
          throw new ExistingObjectException("Description in auction already exists");
         }
         this.description = description;
    }

//    public Auction(BorrowerService.Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand){
//        this(createNewAuctionSinceNowCommand.getLoanAmount(), createNewAuctionSinceNowCommand.getBeginLoanDate(),
//                createNewAuctionSinceNowCommand.getEndDate(), createNewAuctionSinceNowCommand.getBeginLoanDate(),
//                createNewAuctionSinceNowCommand.getEndLoanDate(), createNewAuctionSinceNowCommand.getInstallmentsFrequencyInYear());
//    }

>>>>>>> d6c70d0ec0455dccecc8fa2cdfbddbc12c8b6190
}
