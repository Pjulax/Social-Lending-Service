package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import lombok.*;

import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Borrower {

    private  Long id;
    private  List<Auction> auctions;

}
