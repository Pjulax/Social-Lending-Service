package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.Loan;

import javax.xml.bind.ValidationException;
import java.util.List;

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
     interface Query {
     }
}
