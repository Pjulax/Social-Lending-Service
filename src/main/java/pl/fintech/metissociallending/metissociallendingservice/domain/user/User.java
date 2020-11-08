package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import lombok.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;

import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private  Long id;
    private  List<Auction> auctions;

}
