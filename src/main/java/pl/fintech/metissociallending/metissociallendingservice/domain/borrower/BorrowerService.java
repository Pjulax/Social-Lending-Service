package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionWithOffersDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BorrowerService {
    Auction createNewAuctionSinceNow(Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand);
    Auction addAuctionDescription(Command.AddAuctionDescription addAuctionDescription);
    List<Auction> getAllAuctions();

    AuctionWithOffersDTO getAuctionById(Long id);

    interface Command {
        interface CreateNewAuctionSinceNow extends Command{
            BigDecimal getLoanAmount();
            Date getEndDate();
            Integer getNumberOfInstallments();
            String getDescription();
        }
        interface AddAuctionDescription extends Command{
            Long getAuctionId();
            String getDescription();
        }

    }
    interface Query {
        interface getAuction extends  Query {
            Double getLoanAmount();
            Date getEndDate();
            Integer getNumberOfInstallments();
            String getDescription();
            List<Offer> getOffers();
        }
    }
}
