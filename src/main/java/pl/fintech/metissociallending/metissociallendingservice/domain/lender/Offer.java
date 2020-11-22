package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.util.Date;


@Getter
@Builder
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class Offer {

    private final Long id;
    private final Auction auction;
    @JsonIgnore
    private final User lender;
    private final Date date;
    private final Double annualPercentageRate;

}
