package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BorrowerService {
    Auction createNewAuctionSinceNow(Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand);
    List<Auction> getAllAuctions(Query.GetBorrowersAllAuctions getBorrowersAllAuctionsQuery);

    interface Command {
        interface CreateNewAuctionSinceNow extends Command{
            BigDecimal getLoanAmount();
            Date getEndDate();
            Integer getNumberOfInstallments();
            Long getUserId();
        }

    }
    interface Query {
        interface GetBorrowersAllAuctions extends Query{
            Long getUserId();
        }
    }
}
