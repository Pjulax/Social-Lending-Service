package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.LenderService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class OfferDTO  {
    private Long id;
    private Double proposedAnnualPercentageRate;
    private String lender;
    private Date date;

    public static OfferDTO from(Offer offer){
        return new OfferDTO(offer.getId(), offer.getAnnualPercentageRate(), offer.getLender().getUsername(), offer.getDate());
    }
}
