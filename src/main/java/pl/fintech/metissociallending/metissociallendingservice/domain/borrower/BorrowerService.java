package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BorrowerService {
    Auction createNewAuctionSinceNow(Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand);
    List<Auction> getAllAuctions();

    List<Offer> getAllOffersToAuction(Query.GetAllOffersToAuction getAllOffersToAuctionQuery);

    interface Command {
        interface CreateNewAuctionSinceNow extends Command{
            BigDecimal getLoanAmount();
            Date getEndDate();
            Integer getNumberOfInstallments();
            String getDescription();
        }

    }
    interface Query {
        interface GetAllOffersToAuction extends  Query {
            Long getAuctionId();
        }
    }
}
