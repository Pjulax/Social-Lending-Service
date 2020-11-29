package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionWithOffersDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.OfferDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Allows borrower to
 * <p><ul>
 *  <li>create auction.</li>
 *  <li>see auctions.</li>
 *  <li>see offers applied for auctions.</li>
 *  <li>see taken loans.</li>
 *  <li>pay next installment of given loan.</li>
 * </ul></p>
 * @see BorrowerServiceImpl
 */
public interface BorrowerService {
    Auction createNewAuctionSinceNow(Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand);
    Auction addAuctionDescription(Command.AddAuctionDescription addAuctionDescription);
    List<Auction> getAllAuctions();
    void updateAllAuctionStatus();

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
        interface getAuction extends Query {
            Double getLoanAmount();
            Date getEndDate();
            Integer getNumberOfInstallments();
            String getDescription();
            List<OfferDTO> getOffers();
            Boolean isClosed();
        }
    }
}
