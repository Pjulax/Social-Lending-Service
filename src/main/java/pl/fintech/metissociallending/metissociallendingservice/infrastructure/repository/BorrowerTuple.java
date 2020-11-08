package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Borrower;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="BORROWER")
public class BorrowerTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    List<AuctionTuple> auctions;

    static BorrowerTuple from(Borrower borrower){
        return new BorrowerTuple(borrower.getId(),
                borrower.getAuctions()==null?List.of():
                borrower.getAuctions().stream().map(AuctionTuple::from).collect(Collectors.toList()));
    }
    Borrower toDomain(){
        return Borrower.builder()
                .id(id)
                .auctions(auctions.stream().map(AuctionTuple::toDomain).collect(Collectors.toList()))
                .build();
    }
}
