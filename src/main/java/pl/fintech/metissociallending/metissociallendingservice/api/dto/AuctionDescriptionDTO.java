package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import pl.fintech.metissociallending.metissociallendingservice.api.exception.LengthExceededException;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;

public class AuctionDescriptionDTO implements BorrowerService.Command.AddAuctionDescription {
    private Long auctionId;
    private String description;

    @Override
    public Long getAuctionId() {
        return auctionId;
    }

    @Override
    public String getDescription() {
        if(description!=null && description.length() > 255){
            throw new LengthExceededException("Description is limited to 255 characters");
        }
        return description;
    }
}
