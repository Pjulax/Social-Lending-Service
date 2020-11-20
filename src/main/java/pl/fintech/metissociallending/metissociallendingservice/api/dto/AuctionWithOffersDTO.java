package pl.fintech.metissociallending.metissociallendingservice.api.dto;
import lombok.Builder;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


@Setter
@Builder
public class AuctionWithOffersDTO implements BorrowerService.Query.getAuction {
    private Double loanAmount;
    private Date endDate;
    private Integer numberOfInstallments;
    @Size(  min=3,
            max=255,
            message = "The description '${validatedValue}' must be between {min} and {max} characters long")
    private String description;
    private List<Offer> offers;

    public Double getLoanAmount() {
        return loanAmount;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getNumberOfInstallments() {
        return numberOfInstallments;
    }

    @Override
    public List<Offer> getOffers() {
        return offers;
    }

}
