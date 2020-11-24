package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;


import lombok.*;
import org.hibernate.action.internal.OrphanRemovalAction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserTuple borrower;
    private BigDecimal loanAmount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    private Integer numberOfInstallments;
    private Boolean isClosed;
    @Size(min=3, max=255)
    private String description;

    static AuctionTuple from(Auction auction) {
        return new AuctionTuple(auction.getId(),UserTuple.from(auction.getBorrower()),auction.getLoanAmount(),auction.getBeginDate(),auction.getEndDate(),auction.getNumberOfInstallments(), auction.getIsClosed(), auction.getDescription());
    }
    Auction toDomain(){
        return Auction.builder()
                .beginDate(beginDate)
                .id(id)
                .borrower(borrower.toDomain())
                .loanAmount(loanAmount)
                .endDate(endDate)
                .numberOfInstallments(numberOfInstallments)
                .isClosed(isClosed)
                .description(description)
                .build();
    }

}
