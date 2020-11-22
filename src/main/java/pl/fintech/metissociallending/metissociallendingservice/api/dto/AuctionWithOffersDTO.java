package pl.fintech.metissociallending.metissociallendingservice.api.dto;
import lombok.Builder;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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
    private Boolean isClosed;

    public static AuctionWithOffersDTO fromDomain(Auction auction, List<Offer> offers) {
        return new AuctionWithOffersDTO(auction.getLoanAmount().doubleValue(),
                auction.getEndDate(),
                auction.getNumberOfInstallments(),
                auction.getDescription(),
                offers,
                auction.getIsClosed());
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public List<OfferDTO> getOffers() {
        if(offers.isEmpty())
            return List.of();
        return offers.stream().map(OfferDTO::from).collect(Collectors.toList());
    }

    @Override
    public Boolean isClosed() {
        return isClosed;
    }

}
