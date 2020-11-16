package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;


import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class Auction{
    private final Long id;
    private final BigDecimal loanAmount;
    private final Date beginDate;
    private final Date endDate;
    private final Integer numberOfInstallments;
    private final String description;

//    public Auction(BorrowerService.Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand){
//        this(createNewAuctionSinceNowCommand.getLoanAmount(), createNewAuctionSinceNowCommand.getBeginLoanDate(),
//                createNewAuctionSinceNowCommand.getEndDate(), createNewAuctionSinceNowCommand.getBeginLoanDate(),
//                createNewAuctionSinceNowCommand.getEndLoanDate(), createNewAuctionSinceNowCommand.getInstallmentsFrequencyInYear());
//    }

}
