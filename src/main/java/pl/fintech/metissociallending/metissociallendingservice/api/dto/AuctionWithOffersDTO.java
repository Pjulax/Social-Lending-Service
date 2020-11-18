package pl.fintech.metissociallending.metissociallendingservice.api.dto;
import lombok.Builder;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import java.util.Date;
import java.util.List;


@Setter
@Builder
public class AuctionWithOffersDTO implements BorrowerService.Query.getAuction {
    private Double loanAmount;
    private Date endDate;
    private Integer numberOfInstallments;
    private List<Offer> offers;
    private Boolean isClosed;

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
    } //TODO OfferDTO instead of Offer

    @Override
    public Boolean isClosed() {
        return isClosed;
    }

}
