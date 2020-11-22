package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="OFFER")
public class OfferTuple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AuctionTuple auction;

    @ManyToOne
    private UserTuple lender;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private Double annualPercentageRate;


    static OfferTuple from(Offer offer){
        return new OfferTuple(offer.getId(),
                AuctionTuple.from(offer.getAuction()),
                UserTuple.from(offer.getLender()),
                offer.getDate(),
                offer.getAnnualPercentageRate());
    }
    Offer toDomain() {
        return Offer.builder().date(date)
                .annualPercentageRate(annualPercentageRate)
                .lender(lender.toDomain())
                .auction(auction.toDomain())
                .id(id)
                .build();
    }
}
