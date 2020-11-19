package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
public class AuctionDescriptionDTO implements BorrowerService.Command.AddAuctionDescription {
    private Long auctionId;
    @Size(
            min=3,
            max=255,
            message = "The description '${validatedValue}' must be between {min} and {max} characters long"
    )
    @NotNull(
            message = "The description must be not null"
    )
    private String description;

    @Override
    public Long getAuctionId() {
        return auctionId;
    }

    @Override
    public String getDescription() {

        return description;
    }
}
