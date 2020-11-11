package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import lombok.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private  String username;
    private  String password;
    private  List<Role> roles;
    private  List<Auction> auctions;
    private List<Offer> offers;

    public void addOffer(Offer offer){
        offers.add(offer);
    }
    public void addAuction(Auction auction){
        auctions.add(auction);
    }

    User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
