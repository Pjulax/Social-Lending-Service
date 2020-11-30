package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

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
