package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;

import java.util.Date;


@Getter
@Builder
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class Offer {

    private final Long id;
    private final Auction auction;
    private final Date date;
    private final Double annualPercentageRate;

}
