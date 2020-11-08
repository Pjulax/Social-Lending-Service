package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;


import lombok.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="AUCTION")
public class AuctionTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal loanAmount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginLoanDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endLoanDate;
    private Double installmentsFrequencyInYear;

    static AuctionTuple from(Auction auction) {
        return new AuctionTuple(auction.getId(),auction.getLoanAmount(),auction.getBeginDate(),auction.getEndDate(),auction.getBeginLoanDate(), auction.getEndLoanDate(), auction.getInstallmentsFrequencyInYear());
    }
    Auction toDomain(){
        return Auction.builder()
                .beginDate(beginDate)
                .id(id)
                .loanAmount(loanAmount)
                .endDate(endDate)
                .beginLoanDate(beginLoanDate)
                .endLoanDate(endLoanDate)
                .installmentsFrequencyInYear(installmentsFrequencyInYear)
                .build();
    }

}
