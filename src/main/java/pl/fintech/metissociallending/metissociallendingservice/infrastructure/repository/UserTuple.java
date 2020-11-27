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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Size(min = 8)
    private String password;
    private String account;
    private Double balance;
    private String name;
    private String cardNumber;
    private String expiry;
    private String cvc;
    @ElementCollection(fetch = FetchType.EAGER)
    private  List<RoleTuple> roles;


    static UserTuple from(User user){
        return new UserTuple(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getAccount(),
                user.getBalance(),
                user.getName(),
                user.getCardNumber(),
                user.getExpiry(),
                user.getCvc(),
                user.getRoles()==null?List.of():
                        user.getRoles().stream().map(RoleTuple::from).collect(Collectors.toList())
                );
    }
    User toDomain(){
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .account(account)
                .balance(balance)
                .cardNumber(cardNumber)
                .cvc(cvc)
                .expiry(expiry)
                .name(name)
                .roles(roles==null?List.of():roles.stream().map(RoleTuple::toDomain).collect(Collectors.toList()))
                .build();
    }
}
