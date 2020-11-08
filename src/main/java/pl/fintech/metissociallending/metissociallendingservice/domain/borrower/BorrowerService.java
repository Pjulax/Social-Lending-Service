package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BorrowerService {
    Borrower createBorrower(Command.CreateBorrower createBorrowerCommand);
    Auction createNewAuctionSinceNow(Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand);
    List<Auction> getAllAuctions(Query.GetBorrowersAllAuctions getBorrowersAllAuctionsQuery);

    interface Command {
        interface CreateNewAuctionSinceNow extends  Command{
            BigDecimal getLoanAmount();
            Date getEndDate();
            Date getBeginLoanDate();
            Date getEndLoanDate();
            Double getInstallmentsFrequencyInYear();
            Long getBorrowerId();
        }
        interface CreateBorrower extends Command{
             String getName();
             //TODO add user
        }
    }
    interface Query {
        interface GetBorrowersAllAuctions extends Query{
            Long getBorrowerId();
        }
    }
}
