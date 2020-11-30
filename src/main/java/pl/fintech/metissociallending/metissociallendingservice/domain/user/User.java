package pl.fintech.metissociallending.metissociallendingservice.domain.user;
import lombok.*;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String account;
    private List<Role> roles;
    private String name;
    private String cardNumber;
    private String expiry;
    private String cvc;
}
