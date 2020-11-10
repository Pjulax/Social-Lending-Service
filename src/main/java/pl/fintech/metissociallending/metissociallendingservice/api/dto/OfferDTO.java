package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.LenderService;

@Getter
@Setter
@RequiredArgsConstructor
public class OfferDTO implements LenderService.Command.SubmitOffer {
    private Long auctionId;
    private Double proposedAnnualPercentageRate;

    @Override
    public Long getAuctionId() {
        return auctionId;
    }

    @Override
    public Double getProposedAnnualPercentageRate() {
        return proposedAnnualPercentageRate;
    }
}
