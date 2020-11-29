package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import java.util.List;

/**
 * Allows lender to
 * <p><ul>
 *  <li>submit offer to given auction.</li>
 *  <li>get all offers that lender placed.</li>
 *  <li>cancel offer of given offer</li>
 *  <li>get all available auctions that are placed by borrowers except lender</li>
 *  <li>get all lender investments (borrower loans)</li>
 * </ul></p>
 * @see LenderServiceImpl
 */
public interface LenderService {
     Offer submitOffer(Command.SubmitOffer submitOfferCommand);
     void cancelOffer(Command.CancelOffer cancelOfferCommand);
     List<Offer> getAllOffers();
     List<Auction> getAllAvailableAuctions();

     interface Command  {
          interface SubmitOffer {
               Long getAuctionId();
               Double getProposedAnnualPercentageRate();
          }
          interface  CancelOffer {
               Long getOfferId();
          }
     }

}
