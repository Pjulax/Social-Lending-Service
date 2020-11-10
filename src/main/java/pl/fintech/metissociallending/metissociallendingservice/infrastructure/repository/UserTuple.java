package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import lombok.*;

import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Size;

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
    @Column(unique = true, nullable = false)
    private String username;
    @Size(min = 8)
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private  List<RoleTuple> roles;
    @OneToMany
    List<AuctionTuple> auctions;

    static UserTuple from(User user){
        return new UserTuple(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles()==null?List.of():
                user.getRoles().stream().map(RoleTuple::from).collect(Collectors.toList()),
                user.getAuctions()==null?List.of():
                user.getAuctions().stream().map(AuctionTuple::from).collect(Collectors.toList()));
    }
    User toDomain(){
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .roles(roles.stream().map(RoleTuple::toDomain).collect(Collectors.toList()))
                .auctions(auctions.stream().map(AuctionTuple::toDomain).collect(Collectors.toList()))
                .build();
    }
}
