package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.*;

import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="MYUSER")
public class UserTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    List<AuctionTuple> auctions;

    static UserTuple from(User borrower){
        return new UserTuple(borrower.getId(),
                borrower.getAuctions()==null?List.of():
                borrower.getAuctions().stream().map(AuctionTuple::from).collect(Collectors.toList()));
    }
    User toDomain(){
        return User.builder()
                .id(id)
                .auctions(auctions.stream().map(AuctionTuple::toDomain).collect(Collectors.toList()))
                .build();
    }
}
