package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import lombok.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private  Long id;
    private  String username;
    private  String password;
    private  List<Role> roles;
    private  List<Auction> auctions;

    User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
