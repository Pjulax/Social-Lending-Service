package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface LenderService {
     Offer submitOffer(Command.SubmitOffer submitOfferCommand);
     void cancelOffer(Command.CancelOffer cancelOfferCommand);
     List<Offer> getAllOffers();

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
